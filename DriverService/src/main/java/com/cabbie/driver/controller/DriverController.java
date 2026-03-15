package com.cabbie.driver.controller;

import com.cabbie.driver.dto.DriverRequest;
import com.cabbie.driver.dto.DriverResponse;
import com.cabbie.driver.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/register")
    public ResponseEntity<String> registerDriverInfo(@RequestHeader("X-User-Email") String email, @RequestBody DriverRequest driverRequest)
    {
        driverService.registerDriver(email, driverRequest);
        return ResponseEntity.ok("Driver Added Successfully");
    }

    @GetMapping("/profile")
    public ResponseEntity<DriverResponse> getProfile(
            @RequestHeader("X-User-Email") String email)
    {
        return ResponseEntity.ok(driverService.getDriverByEmail(email));
    }

    @PatchMapping("/update-status")
    public ResponseEntity<String> updateDriverStatus(@RequestHeader("X-User-Email") String email,
                                                     Map<String, String> status)
    {
        boolean updatedStatus = driverService.statusUpdate(status.get("status"), email);
        if(updatedStatus)
            return ResponseEntity.ok("Driver status updated successfully");
        return ResponseEntity.notFound().build();
    }

}
