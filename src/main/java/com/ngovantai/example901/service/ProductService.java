package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto dto);

    ProductDto getProductById(Long id);

    List<ProductDto> getAllProducts();

    ProductDto updateProduct(Long id, ProductDto dto);

    void deleteProduct(Long id);
}
