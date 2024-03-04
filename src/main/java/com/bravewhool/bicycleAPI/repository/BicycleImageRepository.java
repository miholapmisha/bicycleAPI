package com.bravewhool.bicycleAPI.repository;

import com.bravewhool.bicycleAPI.entity.BicycleImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BicycleImageRepository extends CrudRepository<BicycleImage, Long> {

}
