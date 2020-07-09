package com.example.sbchainssioicdoauth2.service;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.repository.SsiApplicationRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DBService {

    @Autowired
    private SsiApplicationRepository ssiAppRepo;

    public void submit(SsiApplication ssiApp) {
        ssiApp.setStatus("completed");
        Optional<SsiApplication> oldApp = ssiAppRepo.findFirstByTaxisAfm(ssiApp.getTaxisAfm());
        if (oldApp.isPresent()) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
        }
        ssiAppRepo.save(ssiApp);
    }

    public void temp(SsiApplication ssiApp) {
        ssiApp.setStatus("temp");
        Optional<SsiApplication> oldApp = ssiAppRepo.findFirstByTaxisAfm(ssiApp.getTaxisAfm());
        if (oldApp.isPresent()) {
            String id = oldApp.get().getId();
            ssiAppRepo.deleteById(id);
        }
        ssiAppRepo.save(ssiApp);
    }

    public Optional<SsiApplication> getByTaxisAfm(String taxisAfm) {
        return ssiAppRepo.findFirstByTaxisAfm(taxisAfm);
    }
}
