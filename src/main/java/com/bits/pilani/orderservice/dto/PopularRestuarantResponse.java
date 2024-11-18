package com.bits.pilani.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PopularRestuarantResponse {

    private Integer restaurantId;

    private Long count;
}
