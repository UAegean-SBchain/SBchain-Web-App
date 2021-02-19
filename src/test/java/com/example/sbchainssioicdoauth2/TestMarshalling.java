package com.example.sbchainssioicdoauth2;

import com.example.sbchainssioicdoauth2.model.pojo.HouseholdMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestMarshalling {

    @Test
    public void testHousholdFromCredential() {
        String h = "[{\"name\":\"ΠΟΛΥΝΙΚΟΣ ΤΣΑΓΓΑΛΗΣ\",\"relation\":\"Σύζυγος\", \"afm\": \"419781493\"}]";
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String>[] householdComposition = (Map<String, String>[]) mapper.readValue((String) h, Map[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        List<HouseholdMember> members = new ArrayList();
    }

}
