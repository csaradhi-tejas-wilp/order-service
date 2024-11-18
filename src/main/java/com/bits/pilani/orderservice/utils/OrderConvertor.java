package com.bits.pilani.orderservice.utils;

import com.bits.pilani.orderservice.dto.CompletedOrderResponse;
import com.bits.pilani.orderservice.dto.OrderRequest;
import com.bits.pilani.orderservice.dto.OrderResponse;
import com.bits.pilani.orderservice.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;

public class OrderConvertor {
    

    public static Order toOrder(OrderRequest orderRequest) throws JsonProcessingException
    {
        Order order = new Order();
        order.setRestaurantId(orderRequest.getRestaurantId());
        order.setItems(orderRequest.getItems());
        order.setRestaurantDiscId(orderRequest.getRestaurantDiscId());
        order.setRestaurantDiscAmt(orderRequest.getRestaurantDiscAmt());
        order.setTotalAmt(orderRequest.getTotalAmt());
        order.setAddress(orderRequest.getAddress());
        order.setKilometers(orderRequest.getKilometers());
        order.setOrderStatus(orderRequest.getOrderStatus());
        order.setDeliveryPersonnelId(orderRequest.getDeliveryPersonnelId());

        return order;
    }

    public static OrderResponse toOrderResponse(Order order)
    {
        OrderResponse orderResponse = new OrderResponse();

        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setRestaurantId(order.getRestaurantId());
        orderResponse.setItems(order.getItems()); 
        orderResponse.setTotalAmt(order.getTotalAmt());
        orderResponse.setRestaurantDiscId(order.getRestaurantDiscId());
        orderResponse.setRestaurantDiscAmt(order.getRestaurantDiscAmt());
        orderResponse.setFinalAmt(order.getFinalAmt());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setStartTime(order.getStartTime());
        orderResponse.setModifiedTime(order.getModifiedTime());
        orderResponse.setEndTime(order.getEndTime());
        orderResponse.setExpectedTime(order.getExpectedTime());
        orderResponse.setAddress(order.getAddress());
        orderResponse.setKilometers(order.getKilometers());
        orderResponse.setDeliveryPersonnelId(order.getDeliveryPersonnelId());

        return orderResponse;
    }

    public static CompletedOrderResponse toCompletedOrderResponse(Order order, String discountCode)
    {
        CompletedOrderResponse orderResponse = new CompletedOrderResponse();

        orderResponse.setOrderId(order.getOrderId());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setRestaurantId(order.getRestaurantId());
        orderResponse.setTotalAmt(order.getTotalAmt());
        orderResponse.setRestaurantDiscId(order.getRestaurantDiscId());
        orderResponse.setRestaurantDiscAmt(order.getRestaurantDiscAmt());
        orderResponse.setFinalAmt(order.getFinalAmt());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setEndTime(order.getEndTime());
        orderResponse.setExpectedTime(order.getExpectedTime());
        orderResponse.setDeliveryPersonnelId(order.getDeliveryPersonnelId());
        orderResponse.setDiscountCode(discountCode);

        return orderResponse;
    }
}
