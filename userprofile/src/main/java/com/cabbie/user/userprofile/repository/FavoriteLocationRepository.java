package com.cabbie.user.userprofile.repository;

import com.cabbie.user.userprofile.entity.FavoriteLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteLocationRepository extends JpaRepository<FavoriteLocation, Long> {
    List<FavoriteLocation> findByEmail(String email);
}
