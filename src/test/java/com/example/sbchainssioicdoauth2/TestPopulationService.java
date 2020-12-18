package com.example.sbchainssioicdoauth2;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.service.PopulateInfoService;
import org.junit.jupiter.api.Test;

public class TestPopulationService {

    @Test
    public void testReflectUpdate() {
        SsiApplication app = new SsiApplication();
        app.setNameLatin("nikos");
        PopulateInfoService.updateObject(app, "nameLatin", "nikos2");
        System.out.println(app.getNameLatin());
    }


}
