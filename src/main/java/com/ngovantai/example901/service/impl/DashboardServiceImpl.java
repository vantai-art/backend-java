package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.repository.BillRepository;
import com.ngovantai.example901.repository.OrderRepository;
import com.ngovantai.example901.repository.ProductRepository;
import com.ngovantai.example901.repository.UserRepository;
import com.ngovantai.example901.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final OrderRepository orderRepository;
    private final BillRepository billRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalOrders", orderRepository.count());
        stats.put("totalBills", billRepository.count());
        stats.put("totalProducts", productRepository.count());
        stats.put("totalUsers", userRepository.count());

        // Tính tổng doanh thu
        BigDecimal totalRevenue = billRepository.findAll().stream()
                .map(bill -> bill.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalRevenue", totalRevenue);

        return stats;
    }

    @Override
    public Map<String, Object> getRevenue(String startDate, String endDate) {
        Map<String, Object> revenue = new HashMap<>();

        // TODO: Implement date filtering
        BigDecimal total = billRepository.findAll().stream()
                .map(bill -> bill.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        revenue.put("total", total);
        revenue.put("startDate", startDate);
        revenue.put("endDate", endDate);

        return revenue;
    }
}