package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.dto.RestaurantTableDto;
import com.ngovantai.example901.entity.RestaurantTable;
import com.ngovantai.example901.repository.RestaurantTableRepository;
import com.ngovantai.example901.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository repository;

    @Override
    @Transactional
    public RestaurantTableDto createTable(RestaurantTableDto dto) {
        if (repository.findByTableNumber(dto.getTableNumber()).isPresent()) {
            throw new RuntimeException("Table number already exists: " + dto.getTableNumber());
        }

        RestaurantTable table = RestaurantTable.builder()
                .tableNumber(dto.getTableNumber())
                .capacity(dto.getCapacity())
                .status(dto.getStatus() != null ? dto.getStatus() : RestaurantTable.TableStatus.FREE)
                .areaName(dto.getAreaName())
                .locationDescription(dto.getLocationDescription())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
        return mapToDto(repository.save(table));
    }

    @Override
    public RestaurantTableDto getTableById(Long id) {
        return repository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }

    @Override
    public RestaurantTableDto getTableByNumber(String tableNumber) {
        return repository.findByTableNumber(tableNumber)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Table not found: " + tableNumber));
    }

    @Override
    public List<RestaurantTableDto> getAllTables() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantTableDto> getActiveTables() {
        return repository.findByIsActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RestaurantTableDto updateTable(Long id, RestaurantTableDto dto) {
        RestaurantTable table = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));

        if (dto.getTableNumber() != null && !dto.getTableNumber().equals(table.getTableNumber())) {
            if (repository.findByTableNumber(dto.getTableNumber()).isPresent()) {
                throw new RuntimeException("Table number already exists: " + dto.getTableNumber());
            }
            table.setTableNumber(dto.getTableNumber());
        }

        if (dto.getCapacity() != null)
            table.setCapacity(dto.getCapacity());
        if (dto.getStatus() != null)
            table.setStatus(dto.getStatus());
        if (dto.getAreaName() != null)
            table.setAreaName(dto.getAreaName());
        if (dto.getLocationDescription() != null)
            table.setLocationDescription(dto.getLocationDescription());
        if (dto.getIsActive() != null)
            table.setIsActive(dto.getIsActive());

        return mapToDto(repository.save(table));
    }

    @Override
    @Transactional
    public void deleteTable(Long id) {
        RestaurantTable table = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));

        if (table.getStatus() == RestaurantTable.TableStatus.OCCUPIED) {
            throw new RuntimeException("Cannot delete table with active order");
        }

        table.setIsActive(false);
        repository.save(table);
    }

    @Override
    public List<RestaurantTableDto> getAvailableTables() {
        return repository.findByStatus(RestaurantTable.TableStatus.FREE)
                .stream()
                .filter(RestaurantTable::getIsActive)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantTableDto> getTablesByArea(String areaName) {
        return repository.findByAreaName(areaName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RestaurantTableDto updateTableStatus(Long id, RestaurantTable.TableStatus status) {
        RestaurantTable table = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));

        table.setStatus(status);
        return mapToDto(repository.save(table));
    }

    @Override
    public List<RestaurantTableDto> getTablesByStatus(RestaurantTable.TableStatus status) {
        return repository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private RestaurantTableDto mapToDto(RestaurantTable table) {
        return RestaurantTableDto.builder()
                .id(table.getId())
                .tableNumber(table.getTableNumber())
                .capacity(table.getCapacity())
                .status(table.getStatus())
                .areaName(table.getAreaName())
                .locationDescription(table.getLocationDescription())
                .isActive(table.getIsActive())
                .createdAt(table.getCreatedAt())
                .updatedAt(table.getUpdatedAt())
                .build();
    }
}