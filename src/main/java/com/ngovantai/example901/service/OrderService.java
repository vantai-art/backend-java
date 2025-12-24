package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.OrderDto;
import com.ngovantai.example901.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    // ✅ THÊM username để biết ai tạo đơn hàng
    Order createOrder(OrderDto dto, String username);

    Order updateOrder(Long id, OrderDto dto);

    void deleteOrder(Long id);
}
