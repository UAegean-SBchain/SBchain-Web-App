package com.example.sbchainssioicdoauth2.utils;

import com.example.sbchainssioicdoauth2.model.entity.SsiApplication;
import com.example.sbchainssioicdoauth2.model.pojo.HouseholdMember;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class AmountCalculator {


    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static BigDecimal getTotalMonthlyValue(SsiApplication ssiApp, LocalDate date){
        BigDecimal paymentThresshold = BigDecimal.valueOf(0);
        List<HouseholdMember> household = ssiApp.getHouseholdComposition();
        final LocalDate referenceDate = date == null? LocalDate.now(): date;

        Long adults = household.stream().filter(h -> calculateAge(LocalDate.parse(h.getDateOfBirth(), formatter), referenceDate) >= 18).count();
        Integer nonPrincipalAdults = adults.intValue();
        Integer minorCount = household.size() - nonPrincipalAdults;

        // remove one adult because the first one has a fixed payment value of 200
        if(nonPrincipalAdults == 0 && minorCount > 0){
            nonPrincipalAdults = minorCount - 1;
        } else if(nonPrincipalAdults == 1 && ssiApp.getParenthood() != null && ssiApp.getParenthood().equals("single") && minorCount > 0){
            minorCount--;
        } else if ((nonPrincipalAdults == 1  && minorCount == 0) || nonPrincipalAdults >=2 ){
            nonPrincipalAdults--;
        } else if(nonPrincipalAdults == 1 && !ssiApp.getParenthood().equals("single")){
            nonPrincipalAdults = nonPrincipalAdults + minorCount -1;
        }
        log.info("adult count :{}, minor count :{}", nonPrincipalAdults, minorCount);

        paymentThresshold = BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(200)
                .add((BigDecimal.valueOf(nonPrincipalAdults).multiply(BigDecimal.valueOf(100))
                        .add(BigDecimal.valueOf(minorCount).multiply(BigDecimal.valueOf(50))))));

        BigDecimal salaries = new BigDecimal(ssiApp.getSalariesR()== null? "0" : ssiApp.getSalariesR()).
                subtract(new BigDecimal(ssiApp.getSalariesR()== null? "0" : ssiApp.getSalariesR()).multiply(BigDecimal.valueOf(0.2)));
        BigDecimal pensions = new BigDecimal(ssiApp.getPensionsR() == null? "0" : ssiApp.getPensionsR());
        BigDecimal farming = new BigDecimal(ssiApp.getFarmingR() == null? "0" : ssiApp.getFarmingR());
        BigDecimal freelance = new BigDecimal(ssiApp.getFreelanceR() == null? "0" : ssiApp.getFreelanceR());
        BigDecimal otherBnfts = new BigDecimal(ssiApp.getOtherBenefitsR() == null? "0" : ssiApp.getOtherBenefitsR());
        BigDecimal deposits = new BigDecimal(ssiApp.getDepositsA() == null? "0" : ssiApp.getDepositsA());
        BigDecimal domesticRe = new BigDecimal(ssiApp.getDomesticRealEstateA() == null? "0" : ssiApp.getDomesticRealEstateA());
        BigDecimal foreignRe = new BigDecimal(ssiApp.getForeignRealEstateA() == null? "0" : ssiApp.getForeignRealEstateA());

        BigDecimal totalIncome = (salaries.add(
                pensions).add(
                farming).add(
                freelance).add(
                otherBnfts).add(
                deposits).add(
                domesticRe).add(
                foreignRe)
        ).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

        log.info("payment check payment thresshold :{}, total income :{}", paymentThresshold, totalIncome);

        if(paymentThresshold.compareTo(totalIncome)<= 0){
            return BigDecimal.ZERO;
        }
        return (paymentThresshold.subtract(totalIncome)).divide(BigDecimal.valueOf(6), 2, RoundingMode.HALF_UP);
    }



    public static Integer calculateAge(LocalDate dateOfBirth, LocalDate referenceDate){
        if ((dateOfBirth != null) && (referenceDate != null)) {
            return Period.between(dateOfBirth, referenceDate).getYears();
        } else {
            return 0;
        }
    }
}
