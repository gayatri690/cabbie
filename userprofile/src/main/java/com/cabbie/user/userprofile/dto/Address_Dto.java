package com.cabbie.user.userprofile.dto;

import lombok.Data;

@Data
public class Address_Dto {

    public Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
