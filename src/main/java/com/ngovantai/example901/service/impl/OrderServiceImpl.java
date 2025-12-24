package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.controller.OrderWebSocketController;
import com.ngovantai.example901.dto.OrderDto;
import com.ngovantai.example901.entity.*;
import com.ngovantai.example901.repository.*;
import com.ngovantai.example901.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CoffeeTableRepository coffeeTableRepository;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final OrderWebSocketController webSocketController;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Order not found with id: " + id));
    }

    @Override
    public Order createOrder(OrderDto dto, String username) {
        // ✅ Lấy thông tin user đang đăng nhập từ JWT
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy user: " + username));

        CoffeeTable table = coffeeTableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new RuntimeException("❌ Table not found with id: " + dto.getTableId()));

        Promotion promotion = null;
        if (dto.getPromotionId() != null) {
            promotion = promotionRepository.findById(dto.getPromotionId())
                    .orElseThrow(() -> new RuntimeException("❌ Promotion not found with id: " + dto.getPromotionId()));
        }

        // ✅ Phân quyền tự động: nếu là STAFF / EMPLOYEE => gán employee
        // nếu là USER => gán user
        User employee = null;
        User customer = null;

        if (creator.getRole() == User.Role.ADMIN || creator.getRole() == User.Role.EMPLOYEE) {
            employee = creator;
        } else if (creator.getRole() == User.Role.USER) {
            customer = creator;
        }

        Order order = Order.builder()
                .table(table)
                .employee(employee)
                .user(customer)
                .promotion(promotion)
                .status(Order.Status.PENDING)
                .notes(dto.getNotes())
                .totalAmount(dto.getTotalAmount())
                .build();

        Order savedOrder = orderRepository.save(order);

        // ✅ Gửi thông báo realtime qua WebSocket (nếu có)
        try {
            webSocketController.notifyNewOrder(savedOrder);
        } catch (Exception e) {
            System.out.println("⚠️ Không gửi được WebSocket: " + e.getMessage());
        }

        return savedOrder;
    }

    @Override
    public Order updateOrder(Long id, OrderDto dto) {
        Order order = getOrderById(id);

        if (dto.getNotes() != null)
            order.setNotes(dto.getNotes());

        if (dto.getStatus() != null)
            order.setStatus(Order.Status.valueOf(dto.getStatus()));

        if (dto.getTotalAmount() != null)
            order.setTotalAmount(dto.getTotalAmount());

        if (dto.getPromotionId() != null) {
            Promotion promo = promotionRepository.findById(dto.getPromotionId())
                    .orElseThrow(() -> new RuntimeException("❌ Promotion not found"));
            order.setPromotion(promo);
        }

        Order updatedOrder = orderRepository.save(order);

        try {
            webSocketController.notifyOrderUpdate(updatedOrder);
        } catch (Exception e) {
            System.out.println("⚠️ Không gửi được WebSocket update: " + e.getMessage());
        }

        return updatedOrder;
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("❌ Cannot delete: Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }
}
