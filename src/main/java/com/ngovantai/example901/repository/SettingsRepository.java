package com.ngovantai.example901.repository;

import com.ngovantai.example901.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}
