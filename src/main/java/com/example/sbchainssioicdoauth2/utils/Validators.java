/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sbchainssioicdoauth2.utils;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;

/**
 *
 * @author nikos
 */
public class Validators {

    public static boolean validateSsiApp(SsiApplication app) {
        return app.getId() != null && app.getUuid() != null
                && app.getTaxisAfm() != null && app.getTaxisFamilyName() != null
                && app.getTaxisFirstName() != null && app.getTaxisFathersName() != null
                && app.getTaxisMothersName() != null && app.getSurnameLatin() != null
                && app.getNameLatin() != null && app.getFatherNameLatin() != null
                && app.getMotherNameLatin() != null && app.getTaxisAmka() != null
                && app.getTaxisDateOfBirth() != null && app.getNationality() != null
                && app.getMaritalStatus() != null && app.getHospitalized() != null
                && app.getHospitalizedSpecific() != null && app.getMonk() != null
                && app.getLuxury() != null && app.getStreet() != null && app.getStreetNumber() != null
                && app.getOwnership() != null && app.getSupplyType() != null
                && app.getMeterNumber() != null && app.getParticipateFead() != null
                && app.getSelectProvider() != null && app.getGender() != null
                && app.getEmploymentStatus() != null && app.getUnemployed() != null
                && app.getEmployed() != null && app.getOaedId() != null
                && app.getOaedDate() != null && app.getEmail() != null
                && app.getMobilePhone() != null && app.getLandline() != null
                && app.getIban() != null && app.getParenthood() != null
                && app.getCustody() != null && app.getAdditionalAdults() != null
                && app.getSalariesR() != null && app.getPensionsR() != null && app.getFarmingR() != null
                && app.getFreelanceR() != null && app.getUnemploymentBenefitR() != null && app.getEkasR() != null
                && app.getOtherIncomeR() != null && app.getOtherBenefitsR() != null && app.getErgomeR() != null
                && app.getDepositInterestA() != null && app.getDomesticRealEstateA() != null && app.getVehicleValueA() != null
                && app.getInvestmentsA() != null;

    }

}
