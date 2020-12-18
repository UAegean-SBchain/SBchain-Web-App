/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sbchainssioicdoauth2.utils;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.model.pojo.Case;

/**
 *
 * @author nikos
 */
public class Wrappers {

    public static Case wrapSiiAppToCase(SsiApplication ssiApp) {

        Case c = new Case();
        c.setUuid(ssiApp.getUuid());
        c.setIsStudent(Boolean.FALSE);
        c.setName(ssiApp.getTaxisAfm());

        return c;
    }

}
