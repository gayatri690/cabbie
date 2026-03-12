package com.cabbie.driver.controller;

import com.cabbie.driver.dto.DriverRequest;
import com.cabbie.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDriverInfo(@RequestBody DriverRequest driverRequest, Authentication authentication)
    {
        String email = authentication.getName();
        driverService.registerDriver(email, driverRequest);
        return ResponseEntity.ok("Driver Added Successfully");
    }


}
