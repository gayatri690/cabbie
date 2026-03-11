package com.cabbie.user.userprofile.dto;

import lombok.Data;

@Data
public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String role;
    private AddressDto addressDto;
}
