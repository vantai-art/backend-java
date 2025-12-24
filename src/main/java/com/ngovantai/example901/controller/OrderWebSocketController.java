package com.ngovantai.example901.controller;

import com.ngovantai.example901.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class OrderWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyNewOrder(Order order) {
        messagingTemplate.convertAndSend("/topic/orders", order);
    }

    public void notifyOrderUpdate(Order order) {
        messagingTemplate.convertAndSend("/topic/orders/update", order);
    }
}