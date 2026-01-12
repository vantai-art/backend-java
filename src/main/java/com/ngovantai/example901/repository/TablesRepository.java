package com.ngovantai.example901.repository;

import com.ngovantai.example901.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TablesRepository extends JpaRepository<Tables, Long> {
    List<Tables> findByStatus(Tables.TableStatus status);
}
