package com.example.sbchainssioicdoauth2.repository;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SsiApplicationRepository extends MongoRepository<SsiApplication, String> {

    public Optional<SsiApplication> findFirstByTaxisAfm(String taxisAfm);

    public List<SsiApplication> findBySubmittedMunicipality(String municipality);

    public List<SsiApplication> findByTaxisAfm(String taxisAfm);

    public Optional<SsiApplication> findByUuid(String uuid);
}
