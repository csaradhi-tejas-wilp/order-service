package com.bits.pilani.orderservice.enums;

public enum OrderStatus {
    PLACED, 
    
    ACCEPTED,

    PREPARING,
    
    READY_FOR_PICKUP,
    
    OUT_FOR_DELIVERY,
    
    DELIVERED,

    REJECTED,

    CANCELLED;

    public OrderStatus getNext() {
        OrderStatus[] statuses = OrderStatus.values();
        int currentIndex = this.ordinal();
        int nextIndex = (currentIndex + 1) % statuses.length;
        return statuses[nextIndex];
    }
}
