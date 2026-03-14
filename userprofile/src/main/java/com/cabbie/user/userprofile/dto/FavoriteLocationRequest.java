package com.cabbie.user.userprofile.dto;

import lombok.Data;

@Data
public class FavoriteLocationRequest {

    private String locationName;
    private Double latitude;
    private Double longitude;
}
