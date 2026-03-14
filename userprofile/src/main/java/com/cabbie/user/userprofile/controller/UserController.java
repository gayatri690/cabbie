package com.cabbie.user.userprofile.controller;


import com.cabbie.user.userprofile.dto.FavoriteLocationRequest;
import com.cabbie.user.userprofile.dto.FavoriteLocationResponse;
import com.cabbie.user.userprofile.dto.UserRequest;
import com.cabbie.user.userprofile.dto.UserResponse;
import com.cabbie.user.userprofile.enums.Role;
import com.cabbie.user.userprofile.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        if(role.equals(Role.ADMIN.name())){
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

    @PatchMapping("/update-phone")
    public ResponseEntity<String> updateMobileNumber(@RequestBody Map<String, String> requestbody,
                                                     @RequestHeader("X-User-Email") String email)
    {
        boolean updateStatus = userService.updatePhoneNumber(requestbody.get("phone"), email);
        if(updateStatus)
            return ResponseEntity.ok("User Phone Number updated successfully");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email)
    {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/add-favoriteLocation")
    public ResponseEntity<String> addFavoriteLocation(@RequestBody FavoriteLocationRequest favoriteLocationRequest,
                                                      @RequestHeader("X-User-Email") String email)
    {
        userService.addFavoriteLocation(favoriteLocationRequest, email);
        return ResponseEntity.ok("Your Favorite location added");
    }

    @GetMapping("/get-favoriteLocation")
    public ResponseEntity<List<FavoriteLocationResponse>> getFavoriteLocation(@RequestHeader("X-User-Email") String email)
    {
        return ResponseEntity.ok(userService.getFavoriteLocations(email));
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<String> deleteAccount(
            @RequestHeader("X-User-Email") String email,
            @RequestHeader("X-User-Role") String role
    )
    {
        if(role.equals(Role.DRIVER.name()) || role.equals(Role.PASSENGER.name())){
            userService.deleteAccount(email);
        }
        return ResponseEntity.ok("Account Deleted Successfully");
    }
}
