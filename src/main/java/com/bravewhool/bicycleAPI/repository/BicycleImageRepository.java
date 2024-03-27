package com.bravewhool.bicycleAPI.repository;

import com.bravewhool.bicycleAPI.entity.BicycleImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BicycleImageRepository extends CrudRepository<BicycleImage, String>, JpaRepository<BicycleImage, String> {

    List<BicycleImage> getBicycleImagesByBicycleId(UUID bicycleId);

    Optional<BicycleImage> getBicycleImageByUrl(String url);

    @Query("SELECT i.url FROM BicycleImage i WHERE i.bicycle.id = ?1")
    List<String> getImageUrlsByBicycleId(UUID bicycleId);

    @Transactional
    @Modifying
    @Query("DELETE FROM BicycleImage i WHERE i.bicycle.id = ?1")
    void removeByBicycleId(UUID id);

    void removeByUrl(String ulr);
}
