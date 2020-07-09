package com.example.sbchainssioicdoauth2.service;

import com.example.sbchainssioicdoauth2.config.MyResourceNotFoundException;
import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.thymeleaf.util.StringUtils;

@Slf4j
@Service
public class PopulateInfoService {

    @Autowired
    DBService dbServ;

    public void populateFetchInfo(ModelMap model, HttpServletRequest request, String uuid) {

        final Principal principal = request.getUserPrincipal();
        if (principal instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken kp = (KeycloakAuthenticationToken) principal;
            kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
            Map<String, Object> otherClaims = kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
            model.addAttribute("ssiInfo", otherClaims);
            model.addAttribute("uuid", uuid);
        } else {
            throw new MyResourceNotFoundException("resource not found or claims are empty");

        }

    }

    public ModelMap mergeModelFromCache(SsiApplication cachedSsiApp, ModelMap map, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException, IntrospectionException, IntrospectionException {

        if (map.get("ssiInfo") != null) {

            //check if this is an already completed application
            final Principal principal = request.getUserPrincipal();
            if (principal instanceof KeycloakAuthenticationToken) {
                KeycloakAuthenticationToken kp = (KeycloakAuthenticationToken) principal;
                kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
                Map<String, Object> otherClaims = kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
                Optional<SsiApplication> oldApp = dbServ.getByTaxisAfm((String) otherClaims.get("taxisAfm"));
                if (oldApp.isPresent()) {
                    oldApp.get().setCompleted(true);
                    cachedSsiApp = oldApp.get();
                }
            }

            BeanInfo beanInfo = Introspector.getBeanInfo(SsiApplication.class);
            for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
                String propertyName = propertyDesc.getName();
                Object value = propertyDesc.getReadMethod().invoke(cachedSsiApp);
                if (value != null) {
                    ((Map) map.get("ssiInfo")).put(propertyName, value);
                }
            }
        }
        return map;
    }

    public SsiApplication populateSsiApp(SsiApplication ssiApp, HttpServletRequest request, String formType, String uuid) {

        final Principal principal = request.getUserPrincipal();
        if (principal instanceof KeycloakAuthenticationToken) {
            KeycloakAuthenticationToken kp = (KeycloakAuthenticationToken) principal;
            kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
            Map<String, Object> otherClaims = kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();

//            if (formType.equals(FormType.PERSONAL_INFO.value)) {
            ssiApp.setSsn(getStringIfNotNull(otherClaims.get("ssn"), ssiApp.getSsn()));
            ssiApp.setTaxisAmka(getStringIfNotNull(otherClaims.get("taxisAmka"), ssiApp.getTaxisAmka()));
            ssiApp.setTaxisAfm(getStringIfNotNull(otherClaims.get("taxisAfm"), ssiApp.getTaxisAfm()));
            ssiApp.setTaxisFamilyName(getStringIfNotNull(otherClaims.get("taxisFamilyName"), ssiApp.getTaxisFamilyName()));
            ssiApp.setTaxisFirstName(getStringIfNotNull(otherClaims.get("taxisFirstName"), ssiApp.getTaxisFirstName()));
            ssiApp.setTaxisFathersName(getStringIfNotNull(otherClaims.get("taxisFathersName"), ssiApp.getTaxisFathersName()));
            ssiApp.setTaxisMothersName(getStringIfNotNull(otherClaims.get("taxisMothersName"), ssiApp.getTaxisMothersName()));
            ssiApp.setTaxisFamilyNameLatin(getStringIfNotNull(otherClaims.get("taxisFamilyNameLatin"), ssiApp.getTaxisFamilyNameLatin()));
            ssiApp.setTaxisFirstNameLatin(getStringIfNotNull(otherClaims.get("taxisFirstNameLatin"), ssiApp.getTaxisFirstNameLatin()));
            ssiApp.setTaxisFathersNameLatin(getStringIfNotNull(otherClaims.get("taxisFathersNameLatin"), ssiApp.getTaxisFathersNameLatin()));
            ssiApp.setTaxisMothersNameLatin(getStringIfNotNull(otherClaims.get("taxisMothersNameLatin"), ssiApp.getTaxisMothersNameLatin()));
            ssiApp.setTaxisDateOfBirth(getStringIfNotNull(otherClaims.get("taxisDateOfBirth"), ssiApp.getTaxisDateOfBirth()));
            ssiApp.setTaxisGender(getStringIfNotNull(otherClaims.get("taxisGender"), ssiApp.getTaxisGender()));
            ssiApp.setNationality(getStringIfNotNull(otherClaims.get("nationality"), ssiApp.getNationality()));
            ssiApp.setMaritalStatus(getStringIfNotNull(otherClaims.get("maritalStatus"), ssiApp.getMaritalStatus()));
            ssiApp.setDisabilityStatus(getStringIfNotNull(otherClaims.get("disabilityStatus"), ssiApp.getDisabilityStatus()));
            ssiApp.setLevelOfEducation(getStringIfNotNull(otherClaims.get("levelOfEducation"), ssiApp.getLevelOfEducation()));
//            if (formType.equals(FormType.PERSONAL_DECLARATION.value)) {
            ssiApp.setHospitalized(getStringIfNotNull(otherClaims.get("hospitalized"), ssiApp.getHospitalized()));
            ssiApp.setHospitalizedSpecific(getStringIfNotNull(otherClaims.get("hospitalizedSpecific"), ssiApp.getHospitalizedSpecific()));
            ssiApp.setMonk(getStringIfNotNull(otherClaims.get("monk"), ssiApp.getMonk()));
            ssiApp.setLuxury(getStringIfNotNull(otherClaims.get("luxury"), ssiApp.getLuxury()));
//            if (formType.equals(FormType.RESIDENCE_INFO.value)) {
            ssiApp.setStreet(getStringIfNotNull(otherClaims.get("street"), ssiApp.getStreet()));
            ssiApp.setStreetNumber(getStringIfNotNull(otherClaims.get("streetNumber"), ssiApp.getStreetNumber()));
            ssiApp.setPo(getStringIfNotNull(otherClaims.get("po"), ssiApp.getPo()));
            ssiApp.setMunicipality(getStringIfNotNull(otherClaims.get("municipality"), ssiApp.getMunicipality()));
            ssiApp.setPrefecture(getStringIfNotNull(otherClaims.get("prefecture"), ssiApp.getPrefecture()));

//            if (formType.equals(FormType.FEAD.value)) {
            ssiApp.setParticipateFead(getStringIfNotNull(otherClaims.get("participateFead"), ssiApp.getParticipateFead()));
            ssiApp.setSelectProvider(getStringIfNotNull(otherClaims.get("feadProvider"), ssiApp.getSelectProvider()));

//            if (formType.equals(FormType.ELECTRICITY_BILL_INFO.value)) {
            ssiApp.setOwnership(getStringIfNotNull(otherClaims.get("ownership"), ssiApp.getOwnership()));
            ssiApp.setSupplyType(getStringIfNotNull(otherClaims.get("supplyType"), ssiApp.getSupplyType()));
            ssiApp.setMeterNumber(getStringIfNotNull(otherClaims.get("meterNumber"), ssiApp.getMeterNumber()));
//            if (formType.equals(FormType.EMPLOYMENT_INFO.value)) {
            ssiApp.setEmploymentStatus(getStringIfNotNull(otherClaims.get("employmentStatus"), ssiApp.getEmploymentStatus()));
            ssiApp.setUnemployed(getStringIfNotNull(StringUtils.isEmpty((String) otherClaims.get("employed")) ? false : true, ssiApp.getUnemployed()));
            ssiApp.setEmployed(getStringIfNotNull(otherClaims.get("employed"), ssiApp.getEmployed()));
            ssiApp.setOaedId(getStringIfNotNull(otherClaims.get("oaedid"), ssiApp.getOaedId()));
            ssiApp.setOaedDate(getStringIfNotNull(otherClaims.get("oaedDate"), ssiApp.getOaedDate()));
            ssiApp.setPo(getStringIfNotNull(otherClaims.get("po"), ssiApp.getPo()));
//            if (formType.equals(FormType.CONTACT_INFO.value)) {
            ssiApp.setEmail(getStringIfNotNull(otherClaims.get("contact-email"), ssiApp.getEmail()));
            ssiApp.setMobilePhone(getStringIfNotNull(otherClaims.get("mobilePhone"), ssiApp.getMobilePhone()));
            ssiApp.setLandline(getStringIfNotNull(otherClaims.get("landline"), ssiApp.getLandline()));
            ssiApp.setIban(getStringIfNotNull(otherClaims.get("iban"), ssiApp.getIban()));
            Map<String, String> mailAddress = new HashMap<>();
            mailAddress.put("street", getStringIfNotNull(otherClaims.get("street"), ssiApp.getStreet()));
            mailAddress.put("streetNumber", getStringIfNotNull(otherClaims.get("streetNumber"), ssiApp.getStreetNumber()));
            mailAddress.put("PO", getStringIfNotNull(otherClaims.get("po"), ssiApp.getPo()));
            mailAddress.put("municipality", getStringIfNotNull(otherClaims.get("municipality"), ssiApp.getMunicipality()));
            mailAddress.put("prefecture", getStringIfNotNull(otherClaims.get("prefecture"), ssiApp.getPrefecture()));
            ssiApp.setMailAddress(mailAddress);
//            if (formType.equals(FormType.PARENTHOOD_INFO.value)) {
            ssiApp.setParenthood(getStringIfNotNull(otherClaims.get("parenthood"), ssiApp.getParenthood()
            ));
            ssiApp.setCustody(getStringIfNotNull(otherClaims.get("custody"), ssiApp.getCustody()));
            ssiApp.setAdditionalAdults(getStringIfNotNull(otherClaims.get("additionalAdults"), ssiApp.getAdditionalAdults()));
//            if (formType.equals(FormType.FINANCIAL_INFO.value)) {
            ssiApp.setSalariesR(getStringIfNotNull(otherClaims.get("salariesR"), ssiApp.getSalariesR()));
            ssiApp.setPensionsR(getStringIfNotNull(otherClaims.get("pensionsR"), ssiApp.getPensionsR()));
            ssiApp.setFarmingR(getStringIfNotNull(otherClaims.get("farmingR"), ssiApp.getFarmingR()));
            ssiApp.setFreelanceR(getStringIfNotNull(otherClaims.get("freelanceR"), ssiApp.getFreelanceR()));
            ssiApp.setRentIncomeR(getStringIfNotNull(otherClaims.get("rentIncomeR"), ssiApp.getRentIncomeR()));
            ssiApp.setUnemploymentBenefitR(getStringIfNotNull(otherClaims.get("unemploymentBenefitR"), ssiApp.getUnemploymentBenefitR()));
            ssiApp.setOtherBenefitsR(getStringIfNotNull(otherClaims.get("otherBenefitsR"), ssiApp.getOtherBenefitsR()));
            ssiApp.setEkasR(getStringIfNotNull(otherClaims.get("ekasR"), ssiApp.getEkasR()));
            ssiApp.setOtherIncomeR(getStringIfNotNull(otherClaims.get("otherIncomeR"), ssiApp.getOtherIncomeR()));
            ssiApp.setErgomeR(getStringIfNotNull(otherClaims.get("ergomeR"), ssiApp.getErgomeR()));

//            if (formType.equals(FormType.ASSET_INFO.value)) {
            ssiApp.setDepositInterestA(getStringIfNotNull(otherClaims.get("depositInterestA"), ssiApp.getDepositInterestA()));
            ssiApp.setDepositsA(getStringIfNotNull(otherClaims.get("depositsA"), ssiApp.getDepositsA()));
            ssiApp.setDomesticRealEstateA(getStringIfNotNull(otherClaims.get("domesticRealEstateA"), ssiApp.getDomesticRealEstateA()));
            ssiApp.setForeignRealEstateA(getStringIfNotNull(otherClaims.get("foreignRealEstateA"), ssiApp.getForeignRealEstateA()));
            ssiApp.setVehicleValueA(getStringIfNotNull(otherClaims.get("vehicleValueA"), ssiApp.getVehicleValueA()));
            ssiApp.setInvestmentsA(getStringIfNotNull(otherClaims.get("investmentsA"), ssiApp.getInvestmentsA()));

//            if (formType.equals(FormType.HOUSEHOLD_COMPOSITION.value)) {
            try {
                if (otherClaims.get("taxis-household") != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String>[] householdComposition = (Map<String, String>[]) mapper.readValue((String) otherClaims.get("taxis-household"), Map[].class);
                    ssiApp.setHouseholdComposition(householdComposition);
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
//            Map<String, String>[] householdComposition = new householdComposition.put(getStringIfNotNull(otherClaims.get("member"), ssiApp.getHouseholdComposition().get("member")), getStringIfNotNull(otherClaims.get("relation"), ssiApp.getHouseholdComposition().get("relation")));
//            }
//            if (formType.equals(FormType.INCOME_GUARANTEE.value)) {
            ssiApp.setMonthlyGuarantee(getStringIfNotNull(otherClaims.get("monthlyGuarantee"), ssiApp.getMonthlyGuarantee()));
            ssiApp.setTotalIncome(getStringIfNotNull(otherClaims.get("totalIncome"), ssiApp.getTotalIncome()));
            ssiApp.setMonthlyIncome(getStringIfNotNull(otherClaims.get("monthlyIncome"), ssiApp.getMonthlyIncome()));
            ssiApp.setMonthlyAid(getStringIfNotNull(otherClaims.get("monthlyAid"), ssiApp.getMonthlyAid()));
//            }

        } else {
            throw new MyResourceNotFoundException("resource not found or claims are empty");

        }
        ssiApp.setUuid(uuid);

        return ssiApp;
    }

    public String getStringIfNotNull(Object newValue, String oldValue) {

        return newValue != null ? String.valueOf(newValue) : oldValue;
    }

}
