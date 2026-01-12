package com.ngovantai.example901.controller;

import com.ngovantai.example901.dto.RestaurantTableDto;
import com.ngovantai.example901.entity.RestaurantTable;
import com.ngovantai.example901.service.RestaurantTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class RestaurantTableController {

    private final RestaurantTableService tableService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RestaurantTableDto> create(@RequestBody RestaurantTableDto dto) {
        return ResponseEntity.ok(tableService.createTable(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTableDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @GetMapping("/number/{tableNumber}")
    public ResponseEntity<RestaurantTableDto> getByNumber(@PathVariable String tableNumber) {
        return ResponseEntity.ok(tableService.getTableByNumber(tableNumber));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantTableDto>> getAll(
            @RequestParam(required = false) Boolean active) {
        if (active != null && active) {
            return ResponseEntity.ok(tableService.getActiveTables());
        }
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/area/{areaName}")
    public ResponseEntity<List<RestaurantTableDto>> getByArea(@PathVariable String areaName) {
        return ResponseEntity.ok(tableService.getTablesByArea(areaName));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RestaurantTableDto>> getByStatus(@PathVariable RestaurantTable.TableStatus status) {
        return ResponseEntity.ok(tableService.getTablesByStatus(status));
    }

    @GetMapping("/available")
    public ResponseEntity<List<RestaurantTableDto>> getAvailableTables() {
        return ResponseEntity.ok(tableService.getAvailableTables());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STAFF')")
    public ResponseEntity<RestaurantTableDto> update(@PathVariable Long id, @RequestBody RestaurantTableDto dto) {
        return ResponseEntity.ok(tableService.updateTable(id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STAFF')")
    public ResponseEntity<RestaurantTableDto> updateStatus(
            @PathVariable Long id,
            @RequestParam RestaurantTable.TableStatus status) {
        return ResponseEntity.ok(tableService.updateTableStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}