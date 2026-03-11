package com.cabbie.user.userprofile.service;

import com.cabbie.user.userprofile.dto.AddressDto;
import com.cabbie.user.userprofile.dto.UserRequest;
import com.cabbie.user.userprofile.dto.UserResponse;
import com.cabbie.user.userprofile.entity.Address;
import com.cabbie.user.userprofile.entity.User;
import com.cabbie.user.userprofile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this :: mapToResponse)
                .toList();
    }

    public boolean updateUser(UserRequest userRequest, String email) {
        return userRepository.findByEmail(email)
                .map(existinguser -> {
                    updateUserFromRequest(existinguser,userRequest);
                    userRepository.save(existinguser);
                    return true;
                }).orElse(false);
    }

    private UserResponse mapToResponse(User user) {

        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getUserrole());

        if(user.getAddress() != null){
            AddressDto addressDto = new AddressDto();
            addressDto.setCity(user.getAddress().getCity());
            addressDto.setState(user.getAddress().getState());
            addressDto.setStreet(user.getAddress().getStreet());
            addressDto.setZipcode(user.getAddress().getZipcode());
            addressDto.setCountry(user.getAddress().getCountry());
            userResponse.setAddressDto(addressDto);
        }
        return userResponse;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

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
