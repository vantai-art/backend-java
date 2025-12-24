package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.dto.SettingsDTO;
import com.ngovantai.example901.entity.Settings;
import com.ngovantai.example901.repository.SettingsRepository;
import com.ngovantai.example901.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {

    private final SettingsRepository settingsRepository;

    @Override
    public Settings getSettings() {
        // Nếu chưa có, tạo mặc định (để không bị null)
        return settingsRepository.findAll().stream().findFirst()
                .orElseGet(() -> {
                    Settings defaults = new Settings();
                    defaults.setStoreName("Coffee Blend");
                    defaults.setCurrency("VND");
                    return settingsRepository.save(defaults);
                });
    }

    @Override
    public Settings updateSettings(SettingsDTO dto) {
        Settings settings = getSettings(); // luôn có 1 bản duy nhất

        settings.setStoreName(dto.getStoreName());
        settings.setStoreEmail(dto.getStoreEmail());
        settings.setStorePhone(dto.getStorePhone());
        settings.setStoreAddress(dto.getStoreAddress());

        settings.setEmailNotifications(dto.getEmailNotifications());
        settings.setOrderNotifications(dto.getOrderNotifications());
        settings.setPromotionNotifications(dto.getPromotionNotifications());

        settings.setCurrency(dto.getCurrency());
        settings.setTimezone(dto.getTimezone());
        settings.setLanguage(dto.getLanguage());
        settings.setTaxRate(dto.getTaxRate());

        settings.setThemeColor(dto.getThemeColor());
        settings.setDarkMode(dto.getDarkMode());
        settings.setFreeShippingThreshold(dto.getFreeShippingThreshold());
        settings.setShippingFee(dto.getShippingFee());

        return settingsRepository.save(settings);
    }
}
