package com.example.sbchainssioicdoauth2.service;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.repository.SsiApplicationRepository;
import com.example.sbchainssioicdoauth2.utils.Validators;
import com.example.sbchainssioicdoauth2.utils.Wrappers;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class DBService {

    @Autowired
    private SsiApplicationRepository ssiAppRepo;

    @Autowired
    private EthereumService ethServ;

    public String submit(SsiApplication ssiApp) {
        ssiApp.setStatus("active");
        ssiApp.setTime(LocalDate.now());
        Optional<SsiApplication> oldApp = ssiAppRepo.findByUuid(ssiApp.getUuid().trim());
        if (oldApp.isPresent() && oldApp.get().getStatus().equals("temp")) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
        }
        //TODO if validation fails to we store the application?
        ssiAppRepo.save(ssiApp);
        if (Validators.validateSsiApp(ssiApp)) {

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
            return response.getBody();

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

}
