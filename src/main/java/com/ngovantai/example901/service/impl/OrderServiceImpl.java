package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.dto.OrderDto;
import com.ngovantai.example901.dto.OrderItemDto;
import com.ngovantai.example901.entity.*;
import com.ngovantai.example901.repository.*;
import com.ngovantai.example901.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableRepository tablesRepository;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository; // ← THÊM DÒN NÀY

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
        // ✅ Tìm user từ JWT username
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("❌ Không tìm thấy user: " + username));

        // ✅ Table có thể null cho đơn hàng online
        RestaurantTable table = null;
        if (dto.getTableId() != null) {
            table = tablesRepository.findById(dto.getTableId())
                    .orElseThrow(() -> new RuntimeException("❌ Table not found with id: " + dto.getTableId()));

            // Cập nhật trạng thái bàn nếu bàn đang trống
            if (table.getStatus() == RestaurantTable.TableStatus.FREE) {
                table.setStatus(RestaurantTable.TableStatus.OCCUPIED);
                tablesRepository.save(table);
            }
        }

        // ✅ Promotion có thể null
        Promotion promotion = null;
        if (dto.getPromotionId() != null) {
            promotion = promotionRepository.findById(dto.getPromotionId())
                    .orElseThrow(() -> new RuntimeException("❌ Promotion not found with id: " + dto.getPromotionId()));
        }

        // ✅ Phân biệt employee và customer dựa trên role
        User employee = null;
        User customer = null;

        if (creator.getRole() == User.Role.ADMIN || creator.getRole() == User.Role.EMPLOYEE) {
            employee = creator;
        } else if (creator.getRole() == User.Role.USER) {
            customer = creator;
        }

        // ✅ Tạo Order với table có thể null
        Order order = Order.builder()
                .table(table)
                .employee(employee)
                .user(customer)
                .promotion(promotion)
                .status(Order.Status.PENDING)
                .notes(dto.getNotes())
                .totalAmount(dto.getTotalAmount())
                .build();

        // ✅ Lưu order trước
        Order savedOrder = orderRepository.save(order);

        // 🔥 TỰ ĐỘNG TẠO ORDER ITEMS từ DTO
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (OrderItemDto itemDto : dto.getItems()) {
                // Validate product tồn tại
                Product product = productRepository.findById(itemDto.getProductId())
                        .orElseThrow(() -> new RuntimeException("❌ Product not found: " + itemDto.getProductId()));

                // Validate quantity
                if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                    throw new RuntimeException("❌ Quantity must be greater than 0");
                }

                // Validate price
                if (itemDto.getPrice() == null || itemDto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new RuntimeException("❌ Price must be greater than 0");
                }

                // Tính subtotal
                BigDecimal subtotal = itemDto.getPrice()
                        .multiply(BigDecimal.valueOf(itemDto.getQuantity()));

                // Tạo OrderItem
                OrderItem item = OrderItem.builder()
                        .order(savedOrder)
                        .product(product)
                        .quantity(itemDto.getQuantity())
                        .price(itemDto.getPrice())
                        .subtotal(subtotal)
                        .build();

                // Thêm vào list items của order
                savedOrder.getItems().add(item);
            }

            // Lưu lại order với items
            savedOrder = orderRepository.save(savedOrder);
        }

        return savedOrder;
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

            // ✅ Chỉ cập nhật trạng thái bàn nếu order có bàn
            if ((newStatus == Order.Status.PAID || newStatus == Order.Status.CANCELLED)
                    && order.getTable() != null) {
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

        // ✅ Chỉ cập nhật trạng thái bàn nếu order có bàn
        if (order.getTable() != null) {
            order.getTable().setStatus(RestaurantTable.TableStatus.FREE);
            tablesRepository.save(order.getTable());
        }

        orderRepository.deleteById(id);
    }
}