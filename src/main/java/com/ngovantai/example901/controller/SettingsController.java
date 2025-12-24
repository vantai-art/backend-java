package com.ngovantai.example901.controller;

import com.ngovantai.example901.dto.SettingsDTO;
import com.ngovantai.example901.entity.Settings;
import com.ngovantai.example901.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public Settings getSettings() {
        return settingsService.getSettings();
    }

    @PutMapping
    public Settings updateSettings(@RequestBody SettingsDTO dto) {
        return settingsService.updateSettings(dto);
    }
}
