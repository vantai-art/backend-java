package com.ngovantai.example901.controller;

import com.ngovantai.example901.dto.OrderItemDto;
import com.ngovantai.example901.entity.OrderItem;
import com.ngovantai.example901.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItem>> getItemsByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.getItemsByOrder(orderId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody OrderItemDto dto) {
        try {
            OrderItem created = orderItemService.createItem(dto);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Lỗi thêm sản phẩm vào đơn: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            orderItemService.deleteItem(id);
            return ResponseEntity.ok("✅ Xóa sản phẩm khỏi đơn hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
    }
}
