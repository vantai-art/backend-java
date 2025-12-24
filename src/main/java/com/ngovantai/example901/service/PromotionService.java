package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.PromotionDTO;

import java.util.List;

public interface PromotionService {
    PromotionDTO createPromotion(PromotionDTO promotionDTO);

    PromotionDTO updatePromotion(Long id, PromotionDTO promotionDTO);

    void deletePromotion(Long id);

    PromotionDTO getPromotionById(Long id);

    List<PromotionDTO> getAllPromotions();
}
