package com.ngovantai.example901.service.impl;

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
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;

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
                .employee(employee)
                .user(customer)
                .promotion(promotion)
                .status(Order.Status.PENDING)
                .notes(dto.getNotes())
                .totalAmount(dto.getTotalAmount())
                .build();

        return orderRepository.save(order);
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

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("❌ Cannot delete: Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }
}