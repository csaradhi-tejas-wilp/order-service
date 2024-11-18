package com.bits.pilani.orderservice.dto;

import java.util.List;

import com.bits.pilani.orderservice.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    private Integer restaurantId;

    private List<MenuItem> items;

    private Float totalAmt;

    private String restaurantDiscId;

    private Float restaurantDiscAmt;

    private String address;
    
    private Integer kilometers;

    private Integer deliveryPersonnelId;

    private OrderStatus orderStatus;

}
