package com.ngovantai.example901.repository;

import com.ngovantai.example901.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findByStatus(RestaurantTable.TableStatus status);

    Optional<RestaurantTable> findByTableNumber(String tableNumber);

    List<RestaurantTable> findByAreaName(String areaName);

    List<RestaurantTable> findByIsActiveTrue();

    @Query("SELECT t FROM RestaurantTable t WHERE t.status = 'FREE' AND t.capacity >= :minCapacity AND t.isActive = true")
    List<RestaurantTable> findAvailableTablesWithMinCapacity(@Param("minCapacity") Integer minCapacity);
}