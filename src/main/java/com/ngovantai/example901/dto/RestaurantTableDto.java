package com.ngovantai.example901.dto;

import com.ngovantai.example901.entity.RestaurantTable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTableDto {
    private Long id;
    private String tableNumber; // Đổi từ Integer thành String
    private Integer capacity;
    private RestaurantTable.TableStatus status;
    private String areaName;
    private String locationDescription;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}