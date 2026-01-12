package com.ngovantai.example901.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant_tables") // Đổi tên bảng
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_number", unique = true)
    private String tableNumber; // Đổi từ Integer thành String (VD: "A01", "VIP-1")

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private TableStatus status = TableStatus.FREE;

    @Column(name = "area_name")
    private String areaName; // Thêm khu vực

    @Column(name = "location_description")
    private String locationDescription; // Thêm mô tả vị trí

    @Column(name = "is_active")
    private Boolean isActive = true;

    // Quan hệ với Order (1 Table có nhiều Order)
    @OneToMany(mappedBy = "table", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum TableStatus {
        FREE, // Trống
        OCCUPIED, // Có khách
        RESERVED, // Đã đặt trước
        CLEANING // Đang dọn dẹp
    }
}