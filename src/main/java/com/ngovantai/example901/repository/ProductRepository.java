package com.ngovantai.example901.repository;

import com.ngovantai.example901.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
