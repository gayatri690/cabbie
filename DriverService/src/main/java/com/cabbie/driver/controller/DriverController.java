package com.cabbie.driver.controller;

import com.cabbie.driver.dto.DriverRequest;
import com.cabbie.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDriverInfo(@RequestHeader("X-User-Email") String email, @RequestBody DriverRequest driverRequest)
    {
        driverService.registerDriver(email, driverRequest);
        return ResponseEntity.ok("Driver Added Successfully");
    }


}
