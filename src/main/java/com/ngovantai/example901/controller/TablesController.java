package com.ngovantai.example901.controller;

import com.ngovantai.example901.dto.TablesDto;
import com.ngovantai.example901.service.TablesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
public class TablesController {

    private final TablesService tableService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TablesDto> create(@RequestBody TablesDto dto) {
        return ResponseEntity.ok(tableService.createTable(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TablesDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @GetMapping
    public ResponseEntity<List<TablesDto>> getAll() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STAFF')")
    public ResponseEntity<TablesDto> update(@PathVariable Long id, @RequestBody TablesDto dto) {
        return ResponseEntity.ok(tableService.updateTable(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<TablesDto>> getAvailableTables() {
        return ResponseEntity.ok(tableService.getAvailableTables());
    }
}