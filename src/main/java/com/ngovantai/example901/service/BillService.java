package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.BillDTO;
import com.ngovantai.example901.entity.Bill;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface BillService {
    List<Bill> getAll();

    Bill getById(Long id);

    Bill create(BillDTO dto);

    ByteArrayInputStream exportPdf(Long billId);
}
