package com.example.sbchainssioicdoauth2;

import com.example.sbchainssioicdoauth2.config.security.KeycloakConfig;
import com.example.sbchainssioicdoauth2.config.security.KeycloakSecurityConfig;
import com.example.sbchainssioicdoauth2.config.security.PathBasedKeycloakConfigResolver;
import com.example.sbchainssioicdoauth2.repository.SsiApplicationRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SbchainSsiOicdOauth2Application.class, KeycloakConfig.class, KeycloakSecurityConfig.class, PathBasedKeycloakConfigResolver.class})
public class TestRepository {


    @Autowired
    SsiApplicationRepository repo;

//    @Test
//    public  void testFindByIBAN(){
//        Optional<SsiApplication> ssiapp = repo.findFirstByIban("asdfas");
//        System.out.println("test");
//    }
}
