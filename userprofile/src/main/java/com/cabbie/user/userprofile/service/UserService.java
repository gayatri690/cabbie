package com.cabbie.user.userprofile.service;

import com.cabbie.user.userprofile.dto.*;
import com.cabbie.user.userprofile.entity.Address;
import com.cabbie.user.userprofile.entity.FavoriteLocation;
import com.cabbie.user.userprofile.entity.User;
import com.cabbie.user.userprofile.repository.FavoriteLocationRepository;
import com.cabbie.user.userprofile.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FavoriteLocationRepository favoriteLocationRepository;

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

    public boolean updatePhoneNumber(String phone, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        user.ifPresent(u -> {
            u.setPhone(phone);
            userRepository.save(u);
        });
        return user.isPresent();
    }

    public void addFavoriteLocation(FavoriteLocationRequest favoriteLocationRequest, String email) {
        List<FavoriteLocation> locations = favoriteLocationRepository.findByEmail(email);
        if(locations.size() >= 5){
            throw new RuntimeException("Maximum favorite locations reached");
        }
        FavoriteLocation favoriteLocation = new FavoriteLocation();
        mapTofavoriteLocation(favoriteLocationRequest, favoriteLocation, email);
        favoriteLocationRepository.save(favoriteLocation);
    }

    public List<FavoriteLocationResponse> getFavoriteLocations(String email)
    {
        return favoriteLocationRepository.findByEmail(email)
                .stream()
                .map(this :: mapToFavoriteLocationsResponse)
                .toList();
    }

    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }

    private FavoriteLocationResponse mapToFavoriteLocationsResponse(FavoriteLocation favoriteLocation) {
        FavoriteLocationResponse favoriteLocationResponse = new FavoriteLocationResponse();
        favoriteLocationResponse.setLocationName(favoriteLocation.getLocationName());
        favoriteLocationResponse.setLongitude(favoriteLocation.getLongitude());
        favoriteLocationResponse.setLatitude(favoriteLocation.getLatitude());
        return favoriteLocationResponse;
    }

    private void mapTofavoriteLocation(FavoriteLocationRequest favoriteLocationRequest, FavoriteLocation favoriteLocation, String email) {
        favoriteLocation.setEmail(email);
        favoriteLocation.setLocationName(favoriteLocationRequest.getLocationName());
        favoriteLocation.setLatitude(favoriteLocationRequest.getLatitude());
        favoriteLocation.setLongitude(favoriteLocationRequest.getLongitude());
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
