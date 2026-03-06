package com.cabbie.user.userprofile.controller;

import com.cabbie.user.userprofile.dto.UserRequest;
import com.cabbie.user.userprofile.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/register-user")
public class UserRegisterController {

    @Autowired
    private UserRegisterService userRegisterService;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest)
    {
        userRegisterService.createUser(userRequest);
        return new ResponseEntity<>("User Added Successfully", HttpStatus.CREATED);
    }

}
