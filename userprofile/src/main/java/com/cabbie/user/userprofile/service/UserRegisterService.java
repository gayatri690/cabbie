package com.cabbie.user.userprofile.service;

import com.cabbie.user.userprofile.dto.UserRequest;
import com.cabbie.user.userprofile.entity.Address;
import com.cabbie.user.userprofile.entity.User;
import com.cabbie.user.userprofile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRegisterService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user, userRequest);
        userRepository.save(user);
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
