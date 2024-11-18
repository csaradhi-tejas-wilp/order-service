package com.bits.pilani.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bits.pilani.orderservice.entity.OrderDetails;

@Repository
public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Long>, JpaSpecificationExecutor<OrderDetails> {
    
    // Count orders per month for a given year
    @Query("SELECT COUNT(o) FROM OrderDetails o WHERE o.orderYear = :year AND o.orderMonth = :month")
    Long countOrdersByMonthAndYear(int month, int year);

    // Count orders per year
    @Query("SELECT COUNT(o) FROM OrderDetails o WHERE o.orderYear = :year")
    Long countOrdersByYear(int year);

    // Get most ordered items
    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItems();

    // Most ordered items based only on cuisineId
    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o WHERE o.cuisineId = :cuisineId GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItemsByCuisineId(int cuisineId);

    // Most ordered items based only on restaurantId
    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o WHERE o.restaurantId = :restaurantId GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItemsByRestaurantId(int restaurantId);

    // Most ordered items based on restaurantId and cuisineId
    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o WHERE o.restaurantId = :restaurantId AND o.cuisineId = :cuisineId GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItemsByRestaurantIdAndCuisineId(int restaurantId, int cuisineId);

    // Most ordered items based on restaurantId and categoryId
    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o WHERE o.restaurantId = :restaurantId AND o.categoryId = :categoryId GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItemsByRestaurantIdAndCategoryId(int restaurantId, int categoryId);

    // Top 5 restaurants based on the number of orders
    @Query("SELECT o.restaurantId, COUNT(o.restaurantId) AS count FROM OrderDetails o GROUP BY o.restaurantId ORDER BY count DESC")
    List<Object[]> findTop5RestaurantsByOrders();

    @Query("SELECT o.restaurantId, COUNT(o.restaurantId) AS orderCount " +
       "FROM OrderDetails o " +
       "WHERE o.cuisineId = :cuisineId " +
       "GROUP BY o.restaurantId " +
       "ORDER BY orderCount DESC")
    List<Object[]> findTopRestaurantsByCuisineId(int cuisineId);

    // All queries above with userId as an additional parameter
    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o WHERE o.userId = :userId AND o.cuisineId = :cuisineId GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItemsByUserAndCuisineId(int userId, int cuisineId);

    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o WHERE o.userId = :userId AND o.restaurantId = :restaurantId GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItemsByUserAndRestaurantId(int userId, int restaurantId);

    @Query("SELECT o.itemId, COUNT(o.itemId) AS count FROM OrderDetails o WHERE o.userId = :userId AND o.restaurantId = :restaurantId AND o.cuisineId = :cuisineId GROUP BY o.itemId ORDER BY count DESC")
    List<Object[]> findMostOrderedItemsByUserAndRestaurantIdAndCuisineId(int userId, int restaurantId, int cuisineId);

    @Query("SELECT o.restaurantId, COUNT(o.restaurantId) AS count FROM OrderDetails o WHERE o.userId = :userId GROUP BY o.restaurantId ORDER BY count DESC")
    List<Object[]> findTop5RestaurantsByOrdersAndUserId(int userId);

    @Query("SELECT o.restaurantId, COUNT(o.restaurantId) AS orderCount " +
       "FROM OrderDetails o " +
       "WHERE o.userId = :userId " +
       "AND o.cuisineId = :cuisineId " +
       "GROUP BY o.restaurantId " +
       "ORDER BY orderCount DESC")
    List<Object[]> findTopRestaurantsByUserIdAndCuisineId(int userId, int cuisineId);
}
