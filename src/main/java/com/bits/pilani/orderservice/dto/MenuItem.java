package com.bits.pilani.orderservice.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@RequiredArgsConstructor
public class MenuItem {
    
    private Integer id;
    
    private String name;

    private Integer categoryId;

    private Integer cuisineId;
    
    private Integer quantity;
}
