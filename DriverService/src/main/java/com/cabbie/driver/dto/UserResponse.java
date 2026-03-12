package com.cabbie.driver.dto;


import com.cabbie.driver.enums.Role;
import lombok.Data;

@Data
public class UserResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Role role;
    private AddressDto addressDto;
}
