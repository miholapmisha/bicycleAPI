package com.bravewhool.bicycleAPI.repository;

import com.bravewhool.bicycleAPI.entity.Bicycle;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BicycleRepository extends CrudRepository<Bicycle, UUID>,
        JpaSpecificationExecutor<Bicycle>,
        PagingAndSortingRepository<Bicycle, UUID> {

    void removeById(UUID id);

    List<Bicycle> findBicycleByName(String name);

    List<Bicycle> findByNameContainingIgnoreCase(String name);

    List<Bicycle> getBicyclesByIdIsIn(List<UUID> ids);

    @Query("SELECT DISTINCT b.color FROM Bicycle b")
    Set<String> getUniqueColors();

    @Query("SELECT MIN(b.price) FROM Bicycle b")
    BigDecimal getMinBicyclePrice();

    @Query("SELECT MAX(b.price) FROM Bicycle b")
    BigDecimal getMaxBicyclePrice();
}
