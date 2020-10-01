package com.example.sbchainssioicdoauth2.model.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SsiApplication {

    @Id
    private String id;

    @NotBlank(message = "uuid is mandatory")
    private String uuid;

    private List<CredsAndExp> credentialIds;

    //personal info
    private String ssn;
    @NotBlank
    private String taxisAfm;
    @NotBlank
    private String taxisFamilyName;
    @NotBlank
    private String taxisFirstName;
    @NotBlank
    private String taxisFathersName;
    @NotBlank
    private String taxisMothersName;
    @NotBlank
    private String surnameLatin;
    @NotBlank
    private String nameLatin;
    @NotBlank
    private String fatherNameLatin;
    @NotBlank
    private String motherNameLatin;
    private String taxisAmka;
    @NotBlank
    private String taxisDateOfBirth;
    private String taxisGender;
    @NotBlank
    private String nationality;
    @NotBlank
    private String maritalStatus;

    //personal declaration
    @NotBlank
    private String hospitalized;
    @NotBlank
    private String hospitalizedSpecific;
    @NotBlank
    private String monk;
    @NotBlank
    private String luxury;

    //residence info
    @NotBlank
    private String street;
    @NotBlank
    private String streetNumber;
    @NotBlank
    private String po;

    private String municipality;

    private String prefecture;
    @NotBlank
    private String ownership;
    @NotBlank
    private String supplyType;
    @NotBlank
    private String meterNumber;

    //Fead
    @NotBlank
    private String participateFead;
    @NotBlank
    private String selectProvider;

    //demographic info
    @NotBlank
    private String gender;

    private String disabilityStatus;
    private String levelOfEducation;

    //employment info
    @NotBlank
    private String employmentStatus;
    @NotBlank
    private String unemployed;
    @NotBlank
    private String employed;
    @NotBlank
    private String oaedId;
    @NotBlank
    private String oaedDate;

    //contact info
    @NotBlank
    private String email;
    @NotBlank
    private String mobilePhone;
    @NotBlank
    private String landline;
    @NotBlank
    private String iban;
    private Map<String, String> mailAddress = new HashMap<>();

    //parenthood info
    @NotBlank
    private String parenthood;
    @NotBlank
    private String custody;
    @NotBlank
    private String additionalAdults;

    //financial info
    @NotBlank
    private String salariesR;
    @NotBlank
    private String pensionsR;
    @NotBlank
    private String farmingR;
    @NotBlank
    private String freelanceR;
    @NotBlank
    private String rentIncomeR;
    @NotBlank
    private String unemploymentBenefitR;
    @NotBlank
    private String otherBenefitsR;
    @NotBlank
    private String ekasR;
    @NotBlank
    private String otherIncomeR;
    @NotBlank
    private String ergomeR;
    @NotBlank
    private String depositInterestA;
    @NotBlank
    private String depositsA;
    @NotBlank
    private String domesticRealEstateA;
    @NotBlank
    private String foreignRealEstateA;
    @NotBlank
    private String vehicleValueA;
    @NotBlank
    private String investmentsA;

    //household composition
    private Map<String, String>[] householdComposition;

    //income guarantee
    private String monthlyGuarantee;
    private String totalIncome;
    private String monthlyIncome;
    private String monthlyAid;

    // helper object
    private boolean savedInDb;

    //the status of the application
    private String status;

    // the municipality this application was sent to
    private String submittedMunicipality;

    // the date of the application
    private LocalDate time;

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    public static class CredsAndExp {

        private String id;
        private String exp;
        private String name;

    }

}
