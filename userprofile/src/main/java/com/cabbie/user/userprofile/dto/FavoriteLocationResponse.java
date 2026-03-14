package com.cabbie.user.userprofile.dto;

import lombok.Data;

@Data
public class FavoriteLocationResponse {
    private String locationName;

    private Double latitude;

    private Double longitude;
}
