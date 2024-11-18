package com.bits.pilani.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bits.pilani.orderservice.entity.Order;
import com.bits.pilani.orderservice.enums.OrderStatus;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findAllByUserId(int userId);

    Order findByUserIdAndOrderId(int orderId, int userId);

    List<Order> findByUserIdAndRestaurantId(int userId, int restaurantId);

    List<Order> findByUserIdAndOrderStatus(int userId, OrderStatus orderStatus);

    List<Order> findAll();

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END " +
           "FROM Order o " +
           "WHERE o.orderStatus NOT IN ('DELIVERED', 'REJECTED', 'CANCELLED') AND " +
           "o.userId = :userId AND " +
           "o.restaurantId = :restaurantId")
    boolean ongoingOrderExistsByUserIdAndRestaurantId(int userId, int restaurantId);
    
}