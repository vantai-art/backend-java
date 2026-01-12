package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.RestaurantTableDto;
import com.ngovantai.example901.entity.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {
    RestaurantTableDto createTable(RestaurantTableDto dto);

    RestaurantTableDto getTableById(Long id);

    RestaurantTableDto getTableByNumber(String tableNumber);

    List<RestaurantTableDto> getAllTables();

    List<RestaurantTableDto> getActiveTables();

    RestaurantTableDto updateTable(Long id, RestaurantTableDto dto);

    void deleteTable(Long id);

    List<RestaurantTableDto> getAvailableTables();

    List<RestaurantTableDto> getTablesByArea(String areaName);

    RestaurantTableDto updateTableStatus(Long id, RestaurantTable.TableStatus status);

    List<RestaurantTableDto> getTablesByStatus(RestaurantTable.TableStatus status);
}