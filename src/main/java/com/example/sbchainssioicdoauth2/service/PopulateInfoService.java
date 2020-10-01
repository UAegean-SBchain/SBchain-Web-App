package com.example.sbchainssioicdoauth2.service;

import com.example.sbchainssioicdoauth2.config.MyResourceNotFoundException;
import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.model.entity.SsiApplication.CredsAndExp;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
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

    public SsiApplication updateModelfromCacheMergeDB(SsiApplication cachedSsiApp, ModelMap map, HttpServletRequest request, String id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException, IntrospectionException, IntrospectionException {

        BeanInfo beanInfo = Introspector.getBeanInfo(SsiApplication.class);

        if (map.get("ssiInfo") != null) {

            //check if this is an already completed application
            final Principal principal = request.getUserPrincipal();
            if (principal instanceof KeycloakAuthenticationToken) {
                KeycloakAuthenticationToken kp = (KeycloakAuthenticationToken) principal;
                kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
                Map<String, Object> otherClaims = kp.getAccount().getKeycloakSecurityContext().getIdToken().getOtherClaims();
                cachedSsiApp.setCredentialIds(addCredentialIdAndIat("credential-id", cachedSsiApp, otherClaims));

//                Optional<SsiApplication> oldApp = dbServ.getByTaxisAfm((String) otherClaims.get("taxisAfm"));
                Optional<SsiApplication> oldApp = dbServ.getByUuid(id);
                if (oldApp.isPresent()) {
                    cachedSsiApp.setSavedInDb(true);
                    //update the values from the DB
                    for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
                        String propertyName = propertyDesc.getName();
                        Object value = propertyDesc.getReadMethod().invoke(oldApp.get());
                        try {
                            Method setter = new PropertyDescriptor(propertyName, cachedSsiApp.getClass()).getWriteMethod();
                            if (value != null && setter != null) {
                                setter.invoke(cachedSsiApp, value);
                            }
                        } catch (IntrospectionException e) {
                            log.error(e.getLocalizedMessage());
                        }
                    }
                }
            }

            //update the view model from the cache
            for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
                String propertyName = propertyDesc.getName();
                Object value = propertyDesc.getReadMethod().invoke(cachedSsiApp);
                if (value != null) {
                    ((Map) map.get("ssiInfo")).put(propertyName, value);
                }
            }
        }

        if (StringUtils.isEmpty(cachedSsiApp.getStatus())) {
            map.put("newApplication", true);
        } else {
            if (cachedSsiApp.getStatus().equals("temp")) {
                map.put("temporary", true);
            } else {
                map.put("finalized", true);
            }
        }

        return cachedSsiApp;
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
            ssiApp.setSurnameLatin(getStringIfNotNull(otherClaims.get("surnameLatin"), ssiApp.getSurnameLatin()));
            ssiApp.setNameLatin(getStringIfNotNull(otherClaims.get("nameLatin"), ssiApp.getNameLatin()));
            ssiApp.setFatherNameLatin(getStringIfNotNull(otherClaims.get("fatherLatin"), ssiApp.getFatherNameLatin()));
            ssiApp.setMotherNameLatin(getStringIfNotNull(otherClaims.get("motherLatin"), ssiApp.getMotherNameLatin()));
            ssiApp.setTaxisDateOfBirth(getStringIfNotNull(otherClaims.get("taxisDateOfBirth"), ssiApp.getTaxisDateOfBirth()));

//            ssiApp.setTaxisGender(getStringIfNotNull(otherClaims.get("taxisGender"), ssiApp.getTaxisGender()));
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
            ssiApp.setStreet(getStringIfNotNull(otherClaims.get("e1-street"), ssiApp.getStreet()));
            ssiApp.setStreetNumber(getStringIfNotNull(otherClaims.get("e1-number"), ssiApp.getStreetNumber()));
            ssiApp.setPo(getStringIfNotNull(otherClaims.get("e1-po"), ssiApp.getPo()));

            ssiApp.setPrefecture(getStringIfNotNull(otherClaims.get("e1-prefecture"), ssiApp.getPrefecture()));

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
            mailAddress.put("street", getStringIfNotNull(otherClaims.get("e1-street"), ssiApp.getStreet()));
            mailAddress.put("streetNumber", getStringIfNotNull(otherClaims.get("e1-number"), ssiApp.getStreetNumber()));
            mailAddress.put("PO", getStringIfNotNull(otherClaims.get("e1-po"), ssiApp.getPo()));
            mailAddress.put("municipality", getStringIfNotNull(otherClaims.get("e1-municipality"), ssiApp.getMunicipality()));
            mailAddress.put("prefecture", getStringIfNotNull(otherClaims.get("e1-prefecture"), ssiApp.getPrefecture()));
            ssiApp.setMailAddress(mailAddress);
//            if (formType.equals(FormType.PARENTHOOD_INFO.value)) {
            if (getStringIfNotNull(otherClaims.get("parenthood"), ssiApp.getParenthood()
            ) != null) {
                ssiApp.setParenthood(getStringIfNotNull(otherClaims.get("parenthood"), ssiApp.getParenthood()
                ));
            } else {
                ssiApp.setParenthood("false");
            }

            ssiApp.setCustody(getStringIfNotNull(otherClaims.get("custody"), ssiApp.getCustody()));
            ssiApp.setAdditionalAdults(getStringIfNotNull(otherClaims.get("additionalAdults"), ssiApp.getAdditionalAdults()));
            ssiApp.setGender(getStringIfNotNull(otherClaims.get("mitro-gender"), ssiApp.getGender()));
            ssiApp.setMunicipality(getStringIfNotNull(otherClaims.get("mitro-municipality"), ssiApp.getMunicipality()));

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
                if (otherClaims.get("e1-householdComposition") != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, String>[] householdComposition = (Map<String, String>[]) mapper.readValue((String) otherClaims.get("e1-householdComposition"), Map[].class);
                    ssiApp.setHouseholdComposition(householdComposition);
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
//            Map<String, String>[] householdComposition = new householdComposition.put(getStringIfNotNull(otherClaims.get("member"), ssiApp.getHouseholdComposition().get("member")), getStringIfNotNull(otherClaims.get("relation"), ssiApp.getHouseholdComposition().get("relation")));
//            }
//            if (formType.equals(FormType.INCOME_GUARANTEE.value)) {
            ssiApp.setMonthlyGuarantee(getStringIfNotNull(otherClaims.get("monthlyGuarantee"), ssiApp.getMonthlyGuarantee()));
//            ssiApp.setTotalIncome(getStringIfNotNull(otherClaims.get("totalIncome"), ssiApp.getTotalIncome()));
            ssiApp.setTotalIncome(calculateTotalIncome(ssiApp));

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

        return newValue != null ? String.valueOf(newValue).trim() : oldValue;
    }

    public List<CredsAndExp> addCredentialIdAndIat(String attributeName, SsiApplication ssiApp, Map<String, Object> otherClaims) {

        boolean exists = ssiApp.getCredentialIds().stream().filter(cid -> {
            return cid.getId().equals((String) otherClaims.get(attributeName));
        }).findFirst().isPresent();

        if (otherClaims.get(attributeName) != null && !exists) {
            String credentialId = (String) otherClaims.get(attributeName);
            String exp = (String) otherClaims.get("expires");
            String name = (String) otherClaims.get("credential-name");

            CredsAndExp cdi = new SsiApplication.CredsAndExp();
            cdi.setId(credentialId);
            cdi.setExp(exp);
            cdi.setName(name);
            ssiApp.getCredentialIds().add(cdi);
        }

        return ssiApp.getCredentialIds();
    }

    private String calculateTotalIncome(SsiApplication app) {
        int sum = 0;
        sum += getLongOrZero(app.getDepositInterestA());
        sum += getLongOrZero(app.getDepositsA());
        sum += getLongOrZero(app.getEkasR());
        sum += getLongOrZero(app.getErgomeR());
        sum += getLongOrZero(app.getFarmingR());
        sum += getLongOrZero(app.getFreelanceR());
        sum += getLongOrZero(app.getInvestmentsA());
        sum += getLongOrZero(app.getMonthlyAid());
        sum += getLongOrZero(app.getOtherBenefitsR());
        sum += getLongOrZero(app.getOtherIncomeR());
        sum += getLongOrZero(app.getRentIncomeR());
        sum += getLongOrZero(app.getSalariesR());
        sum += getLongOrZero(app.getUnemploymentBenefitR());
        return String.valueOf(sum);
    }

    private Long getLongOrZero(String value) {
        try {
            if (!StringUtils.isEmpty(value)) {
                return Long.parseLong(value);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new Long(0);

    }

}
