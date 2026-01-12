package com.ngovantai.example901.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private Long tableId;
    private Long employeeId;
    private Long userId;
    private String status;
    private BigDecimal totalAmount;
    private Long promotionId;
    private String notes;
    private List<OrderItemDto> items;
}