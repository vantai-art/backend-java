package com.ngovantai.example901.controller;

import com.ngovantai.example901.dto.OrderDto;
import com.ngovantai.example901.entity.Order;
import com.ngovantai.example901.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderDto dto, Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(401).body("⚠️ Bạn chưa đăng nhập!");
            }

            // 🔥 Lấy username từ JWT
            String username = principal.getName();

            Order createdOrder = orderService.createOrder(dto, username);
            return ResponseEntity.ok(createdOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable Long id, @RequestBody OrderDto dto) {
        return orderService.updateOrder(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
