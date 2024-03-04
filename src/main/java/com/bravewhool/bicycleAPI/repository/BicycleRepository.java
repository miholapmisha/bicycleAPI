package com.bravewhool.bicycleAPI.repository;

import com.bravewhool.bicycleAPI.entity.Bicycle;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BicycleRepository extends CrudRepository<Bicycle, Long>,
        JpaSpecificationExecutor<Bicycle>,
        PagingAndSortingRepository<Bicycle, Long> {

    void removeById(Long id);

    List<Bicycle> findBicycleByName(String name);

    List<Bicycle> findByNameContainingIgnoreCase(String name);

    @Query("SELECT DISTINCT b.color FROM Bicycle b")
    Set<String> getUniqueColors();
}
