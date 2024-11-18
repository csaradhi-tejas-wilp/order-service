package com.bits.pilani.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PopularItemResponse {

    private Integer itemId;

    private Long count;
}
