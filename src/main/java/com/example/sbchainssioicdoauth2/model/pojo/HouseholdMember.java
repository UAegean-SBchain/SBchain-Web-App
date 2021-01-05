package com.example.sbchainssioicdoauth2.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class HouseholdMember {
    
    private String name;
    private String surname;
    private String afm;
    private String dateOfBirth;
    private String relationship;
}
