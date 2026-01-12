package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.TablesDto;

import java.util.List;

public interface TablesService {
    TablesDto createTable(TablesDto dto);

    TablesDto getTableById(Long id);

    List<TablesDto> getAllTables();

    TablesDto updateTable(Long id, TablesDto dto);

    void deleteTable(Long id);

    List<TablesDto> getAvailableTables();
}
