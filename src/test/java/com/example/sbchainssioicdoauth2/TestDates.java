package com.example.sbchainssioicdoauth2;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

//import java.sql.Date;


public class TestDates {

    @Test
    public void testDates(){
        String dateS = "1502105926";
        Long ld = Long.parseLong(dateS);
        Date d = Date.from(Instant.ofEpochSecond( Long.parseLong(dateS)));
        System.out.println("The time is " + d.toString());


    }
}
