package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.dto.TablesDto;
import com.ngovantai.example901.entity.Tables;
import com.ngovantai.example901.repository.TablesRepository;
import com.ngovantai.example901.service.TablesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TablesServiceImpl implements TablesService {

    private final TablesRepository repository;

    @Override
    public TablesDto createTable(TablesDto dto) {
        Tables table = Tables.builder()
                .number(dto.getNumber())
                .capacity(dto.getCapacity())
                .status(dto.getStatus())
                .build();
        return mapToDto(repository.save(table));
    }

    @Override
    public TablesDto getTableById(Long id) {
        return repository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Table not found"));
    }

    @Override
    public List<TablesDto> getAllTables() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TablesDto updateTable(Long id, TablesDto dto) {
        Tables table = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        table.setNumber(dto.getNumber());
        table.setCapacity(dto.getCapacity());
        table.setStatus(dto.getStatus());

        return mapToDto(repository.save(table));
    }

    @Override
    public void deleteTable(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<TablesDto> getAvailableTables() {
        return repository.findByStatus(Tables.TableStatus.FREE)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private TablesDto mapToDto(Tables table) {
        return TablesDto.builder()
                .id(table.getId())
                .number(table.getNumber())
                .capacity(table.getCapacity())
                .status(table.getStatus())
                .build();
    }
}
