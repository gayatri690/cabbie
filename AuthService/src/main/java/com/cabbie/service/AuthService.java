package com.cabbie.service;

import com.cabbie.dto.AuthResponse;
import com.cabbie.dto.LoginRequest;
import com.cabbie.dto.RegisterRequest;
import com.cabbie.entity.Address;
import com.cabbie.entity.User;
import com.cabbie.enums.Role;
import com.cabbie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public void register(RegisterRequest userRequest){

        if(userRepository.findByEmail(userRequest.getEmail()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        updateUserFromRequest(user, userRequest);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        //String token = jwtService.generateToken(userDetails);
        String token = jwtService.generateToken(userDetails, user.getUserrole().name());

        return new AuthResponse(token);
    }

    private void updateUserFromRequest(User user, RegisterRequest userRequest) {

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        if(userRequest.getRole() != null){
            user.setUserrole(Role.valueOf(userRequest.getRole().toString()));
        }
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        if(userRequest.getAddressDto() != null){
            Address address = new Address();
            address.setCity(userRequest.getAddressDto().getCity());
            address.setState(userRequest.getAddressDto().getState());
            address.setStreet(userRequest.getAddressDto().getStreet());
            address.setZipcode(userRequest.getAddressDto().getZipcode());
            address.setCountry(userRequest.getAddressDto().getCountry());
            user.setAddress(address);
        }
    }
}
