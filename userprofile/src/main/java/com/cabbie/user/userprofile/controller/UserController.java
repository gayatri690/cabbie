package com.cabbie.user.userprofile.controller;


import com.cabbie.user.userprofile.dto.UserRequest;
import com.cabbie.user.userprofile.dto.UserResponse;
import com.cabbie.user.userprofile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role
    ) {

        if(role.equals("ADMIN")){
            return ResponseEntity.ok(userService.getAllUsers());
        }
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(
            @RequestBody UserRequest userRequest,
            @RequestHeader("X-User-Email") String email)
    {
        boolean updateStatus = userService.updateUser(userRequest,email);
        if(updateStatus)
            return ResponseEntity.ok("User updated successfully");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email)
    {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}
