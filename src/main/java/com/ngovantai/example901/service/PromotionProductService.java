package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.PromotionProductDto;
import java.util.List;

public interface PromotionProductService {
    PromotionProductDto create(PromotionProductDto dto);

    PromotionProductDto update(Long id, PromotionProductDto dto);

    List<PromotionProductDto> getAll();

    PromotionProductDto getById(Long id);

    void delete(Long id);
}
