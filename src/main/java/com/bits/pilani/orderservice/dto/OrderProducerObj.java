package com.bits.pilani.orderservice.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProducerObj {

    private int orderId;
    //@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String expectedTime;

    @Override
    public String toString() {
        return this.orderId + "|" + this.expectedTime;
    }
}
