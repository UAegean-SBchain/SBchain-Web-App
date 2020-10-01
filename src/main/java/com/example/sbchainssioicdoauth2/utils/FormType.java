package com.example.sbchainssioicdoauth2.utils;

public enum FormType {

    PERSONAL_INFO("personalInfo"),
    PERSONAL_DECLARATION("personalDeclaration"),
    RESIDENCE_INFO("residenceInfo"),
    ELECTRICITY_BILL_INFO("electricityBillInfo"),
    FEAD("fead"),
    DEMOGRAPHIC_INFO("demographicInfo"),
    EMPLOYMENT_INFO("employmentInfo"),
    CONTACT_INFO("contactInfo"),
    PARENTHOOD_INFO("parenthoodInfo"),
    FINANCIAL_INFO("financialInfo"),
    ASSET_INFO("assetInfo"),
    HOUSEHOLD_COMPOSITION("householdComposition"),
    INCOME_GUARANTEE("incomeGuarantee");

    public final String value;

    private FormType(String value) {
        this.value = value;

    }

}
