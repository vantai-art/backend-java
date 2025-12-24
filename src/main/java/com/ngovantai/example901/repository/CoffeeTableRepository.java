package com.ngovantai.example901.repository;

import com.ngovantai.example901.entity.CoffeeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoffeeTableRepository extends JpaRepository<CoffeeTable, Long> {
    List<CoffeeTable> findByStatus(CoffeeTable.TableStatus status);
}
