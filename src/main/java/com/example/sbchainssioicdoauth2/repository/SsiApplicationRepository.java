package com.example.sbchainssioicdoauth2.repository;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SsiApplicationRepository extends MongoRepository<SsiApplication, String> {

    public Optional<SsiApplication> findFirstByTaxisAfm(String taxisAfm);

}
