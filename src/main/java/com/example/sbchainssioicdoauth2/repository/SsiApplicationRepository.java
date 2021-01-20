package com.example.sbchainssioicdoauth2.repository;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SsiApplicationRepository extends MongoRepository<SsiApplication, String> {

    public Optional<SsiApplication> findFirstByTaxisAfm(String taxisAfm);

    public Optional<SsiApplication> findFirstByIban(String iban);
    public List<SsiApplication> findByIban(String iban);

    public List<SsiApplication> findBySubmittedMunicipality(String municipality);

    public List<SsiApplication> findByTaxisAfm(String taxisAfm);

    public Optional<SsiApplication> findByUuid(String uuid);
}
