package com.ngovantai.example901.service.impl;

import com.ngovantai.example901.dto.OrderItemDto;
import com.ngovantai.example901.entity.Order;
import com.ngovantai.example901.entity.OrderItem;
import com.ngovantai.example901.entity.Product;
import com.ngovantai.example901.repository.OrderItemRepository;
import com.ngovantai.example901.repository.OrderRepository;
import com.ngovantai.example901.repository.ProductRepository;
import com.ngovantai.example901.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderItem> getItemsByOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("❌ Order not found with id: " + orderId));
        return orderItemRepository.findByOrder(order);
    }

    @Override
    public OrderItem createItem(OrderItemDto dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("❌ Order not found with id: " + dto.getOrderId()));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("❌ Product not found with id: " + dto.getProductId()));

        if (dto.getQuantity() == null || dto.getQuantity() <= 0)
            throw new RuntimeException("❌ Quantity must be greater than 0");

        if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new RuntimeException("❌ Price must be greater than 0");

        OrderItem item = OrderItem.builder()
                .order(order)
                .product(product)
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .subtotal(dto.getPrice().multiply(BigDecimal.valueOf(dto.getQuantity())))
                .build();

        return orderItemRepository.save(item);
    }

    @Override
    public void deleteItem(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new RuntimeException("❌ Cannot delete: OrderItem not found with id " + id);
        }
        orderItemRepository.deleteById(id);
    }
}
