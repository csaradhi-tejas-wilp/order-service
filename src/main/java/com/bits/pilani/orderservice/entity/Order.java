package com.bits.pilani.orderservice.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bits.pilani.orderservice.dto.MenuItem;
import com.bits.pilani.orderservice.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="order", schema="public")
@EntityListeners({AuditingEntityListener.class})
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private Integer orderId;

    @Column(nullable = false, name="user_id")
    private Integer userId;

    @Column(nullable = false, name="restaurant_id")
    private Integer restaurantId;

    @Column(nullable = false)
    private String items;

    @Column(nullable = false)
    private Float totalAmt;

    @Column(name="restaurant_disc_id")
    private String restaurantDiscId;

    @Column(name="restaurant_disc_amt")
    private Float restaurantDiscAmt;

    @Column(nullable = false, name="final_amt")
    private Float finalAmt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name="order_status")
    private OrderStatus orderStatus;

    @Column(nullable = false, name="start_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @Column(nullable = false, name="modified_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @LastModifiedDate
    private LocalDateTime modifiedTime;

    @Column(name="end_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @Column(nullable = false, name="expected_time")
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expectedTime;

    //TODO: store a discount coupon somewhere if end time doesn't match expected end time.
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer kilometers;

    private Integer deliveryPersonnelId;

    public List<MenuItem> getItems() {
        // Deserialize the JSON string back into a list of MenuItem
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(items, new TypeReference<List<MenuItem>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void setItems(List<MenuItem> items) {
        // Serialize the list of MenuItem into a JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.items = objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
