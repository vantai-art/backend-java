package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.PaymentRequest;
import java.util.Map;

public interface PaymentService {
    String createVNPayPayment(PaymentRequest request);

    boolean verifyVNPayCallback(Map<String, String> params);

    String createMoMoPayment(PaymentRequest request);
}