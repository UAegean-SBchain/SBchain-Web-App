package com.example.sbchainssioicdoauth2.service;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.repository.SsiApplicationRepository;
import com.example.sbchainssioicdoauth2.utils.AmountCalculator;
import com.example.sbchainssioicdoauth2.utils.Validators;
import com.example.sbchainssioicdoauth2.utils.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DBService {

    @Autowired
    private SsiApplicationRepository ssiAppRepo;

    @Autowired
    private EthereumService ethServ;

    public String submit(SsiApplication ssiApp) {

        ssiApp.setTime(LocalDate.now());
        Optional<SsiApplication> oldApp = ssiAppRepo.findByUuid(ssiApp.getUuid().trim());
        if (oldApp.isPresent() && oldApp.get().getStatus().equals("temp")) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
        }

        List<SsiApplication> existingApplications = ssiAppRepo.findByTaxisAfm(ssiApp.getTaxisAfm());
        long afmConflicts = existingApplications.stream().filter(previousApps ->
                previousApps.getTaxisAfm().equals(ssiApp.getTaxisAfm()) && previousApps.getStatus().equals("active")
        ).count();

        List<SsiApplication> conflictingIBANS = ssiAppRepo.findByIban(ssiApp.getIban().trim());
        boolean ibanConflicts =
                conflictingIBANS.stream().filter(app -> {
                    return app.getStatus().equals("ACCEPTED");
                }).count() > 0;

        if (Validators.validateSsiApp(ssiApp) && afmConflicts == 0 && !ibanConflicts) {
            ssiApp.setStatus("active");
            ssiAppRepo.save(ssiApp);

            // convert ssiApp to monitoredCase
            ethServ.addCase(Wrappers.wrapSiiAppToCase(ssiApp));

            //TODO add delay here
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("uuid", ssiApp.getUuid());
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            String monitorServiceUrl = StringUtils.isEmpty(System.getenv("MONITOR_URL")) ? "http://localhost:8081" : System.getenv("MONITOR_URL");
            monitorServiceUrl = monitorServiceUrl + "/validate-update";

            ResponseEntity<String> response
                    = restTemplate.exchange(monitorServiceUrl,
                    HttpMethod.POST,
                    entity,
                    String.class);
            log.info(response.getBody());
            if(!response.getBody().equals("OK")){
                ssiApp.setStatus("rejected");
                ssiAppRepo.save(ssiApp);
                return "FAIL";
            }
            String amount = AmountCalculator.getTotalMonthlyValue(ssiApp, null).toString();
            log.info("final amount {}", amount);
            return amount;

        } else {
            return "FAIL";
        }

    }

    public void temp(SsiApplication ssiApp) {
        ssiApp.setStatus("temp");
        ssiApp.setTime(LocalDate.now());

        Optional<SsiApplication> oldApp = ssiAppRepo.findByUuid(ssiApp.getUuid().trim());
        if (oldApp.isPresent()) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
        }
        ssiAppRepo.save(ssiApp);
    }

    public Optional<SsiApplication> getByTaxisAfm(String taxisAfm) {
        return ssiAppRepo.findFirstByTaxisAfm(taxisAfm.trim());
    }

    public Optional<SsiApplication> getByUuid(String id) {
        return ssiAppRepo.findByUuid(id.trim());
    }

    public Optional<List<SsiApplication>> getByMunicipality(String municipality) {
        return Optional.of(ssiAppRepo.findBySubmittedMunicipality(municipality.trim()));
    }

    public List<SsiApplication> findAllByAFM(String afm) {
        return ssiAppRepo.findByTaxisAfm(afm.trim());
    }

    public void expire(SsiApplication ssiApp) {
        ssiApp.setStatus("expired");
        Optional<SsiApplication> oldApp = ssiAppRepo.findById(ssiApp.getId());
        if (oldApp.isPresent()) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
        }
        ssiAppRepo.save(ssiApp);
    }

    public void delete(SsiApplication ssiApp) {
        Optional<SsiApplication> oldApp = ssiAppRepo.findById(ssiApp.getId());
        if (oldApp.isPresent()) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
            //delete the application from the blockchain as well
            ethServ.deleteCaseByUuid(id);
        }
    }


    public String replace(SsiApplication ssiApp) {
        ssiApp.setStatus("active");
        ssiApp.setTime(LocalDate.now());
        Optional<SsiApplication> oldApp = ssiAppRepo.findByUuid(ssiApp.getUuid().trim());
        if (oldApp.isPresent() && oldApp.get().getStatus().equals("temp")) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
        }
        ssiAppRepo.save(ssiApp);
        return "OK";

    }


}
