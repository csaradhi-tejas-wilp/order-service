package com.bits.pilani.orderservice.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bits.pilani.orderservice.dto.OrderProducerObj;

@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.order}")
    private String orderReadyQueue;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderReady(String orderProducerObj) {
        rabbitTemplate.convertAndSend(orderReadyQueue, orderProducerObj);
    }
}
