package com.cabbie.user.userprofile.dto;


import com.cabbie.user.userprofile.enums.Role;
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
