package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.dto.OrderDto;
import com.ngovantai.example901.entity.*;
import com.ngovantai.example901.repository.*;
import com.ngovantai.example901.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableRepository tablesRepository; // Đổi từ TablesRepository
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
    @Transactional
    public Order createOrder(OrderDto dto, String username) {
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy user: " + username));

        RestaurantTable table = tablesRepository.findById(dto.getTableId()) // Đổi từ Tables sang RestaurantTable
                .orElseThrow(() -> new RuntimeException("❌ Table not found with id: " + dto.getTableId()));

        // Cập nhật trạng thái bàn
        if (table.getStatus() == RestaurantTable.TableStatus.FREE) {
            table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
            tablesRepository.save(table);
        }

        Promotion promotion = null;
        if (dto.getPromotionId() != null) {
            promotion = promotionRepository.findById(dto.getPromotionId())
                    .orElseThrow(() -> new RuntimeException("❌ Promotion not found with id: " + dto.getPromotionId()));
        }

        User employee = null;
        User customer = null;

        if (creator.getRole() == User.Role.ADMIN || creator.getRole() == User.Role.EMPLOYEE) {
            employee = creator;
        } else if (creator.getRole() == User.Role.USER) {
            customer = creator;
        }

        Order order = Order.builder()
                .table(table) // Set table
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
    @Transactional
    public Order updateOrder(Long id, OrderDto dto) {
        Order order = getOrderById(id);

        if (dto.getNotes() != null)
            order.setNotes(dto.getNotes());

        if (dto.getStatus() != null) {
            Order.Status newStatus = Order.Status.valueOf(dto.getStatus());
            order.setStatus(newStatus);

            // Nếu order đã thanh toán, cập nhật trạng thái bàn
            if (newStatus == Order.Status.PAID || newStatus == Order.Status.CANCELLED) {
                order.getTable().setStatus(RestaurantTable.TableStatus.FREE);
                tablesRepository.save(order.getTable());
            }
        }

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
    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);

        // Cập nhật trạng thái bàn trước khi xóa order
        order.getTable().setStatus(RestaurantTable.TableStatus.FREE);
        tablesRepository.save(order.getTable());

        orderRepository.deleteById(id);
    }
}