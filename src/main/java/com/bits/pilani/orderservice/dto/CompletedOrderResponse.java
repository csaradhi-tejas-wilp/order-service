package com.bits.pilani.orderservice.dto;

import java.time.LocalDateTime;

import com.bits.pilani.orderservice.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletedOrderResponse implements OrderResponseDTO{

    private Integer orderId;
    private Integer userId;
    private Integer restaurantId;
    private Float totalAmt;
    private String restaurantDiscId;
    private Float restaurantDiscAmt;
    private Float finalAmt;
    private OrderStatus orderStatus;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expectedTime;

    private Integer deliveryPersonnelId;

    private String discountCode;
}
