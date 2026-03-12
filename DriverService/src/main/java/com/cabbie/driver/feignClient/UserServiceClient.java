package com.cabbie.driver.feignClient;

import com.cabbie.driver.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="userprofile", url = "http://localhost:8082")
public interface UserServiceClient {

    @GetMapping("/users/findbyemail/{email}")
    UserResponse getUserByEmail(@PathVariable String email);
}
