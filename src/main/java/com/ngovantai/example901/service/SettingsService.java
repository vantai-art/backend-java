package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.SettingsDTO;
import com.ngovantai.example901.entity.Settings;

public interface SettingsService {
    Settings getSettings();

    Settings updateSettings(SettingsDTO dto);
}
