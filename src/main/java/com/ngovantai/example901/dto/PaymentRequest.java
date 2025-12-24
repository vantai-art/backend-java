package com.ngovantai.example901.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long billId;
    private BigDecimal amount;
    private String orderInfo;
}