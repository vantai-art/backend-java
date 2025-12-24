package com.ngovantai.example901.service;

import com.ngovantai.example901.dto.OrderItemDto;
import com.ngovantai.example901.entity.OrderItem;
import java.util.List;

public interface OrderItemService {

    List<OrderItem> getItemsByOrder(Long orderId);

    OrderItem createItem(OrderItemDto dto);

    void deleteItem(Long id);
}
