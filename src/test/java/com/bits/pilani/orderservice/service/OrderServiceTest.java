package com.bits.pilani.orderservice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bits.pilani.orderservice.exception.CustomException;
import com.bits.pilani.orderservice.dto.CompletedOrderResponse;
import com.bits.pilani.orderservice.dto.MenuItem;
import com.bits.pilani.orderservice.dto.OrderRequest;
import com.bits.pilani.orderservice.dto.OrderResponse;
import com.bits.pilani.orderservice.dto.OrderResponseDTO;
import com.bits.pilani.orderservice.entity.Order;
import com.bits.pilani.orderservice.enums.OrderStatus;
import com.bits.pilani.orderservice.repository.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class OrderServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @InjectMocks
    private OrderService orderService;

    private Order mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockOrder = new Order();
        mockOrder.setOrderId(1);
        mockOrder.setUserId(1);
        mockOrder.setRestaurantId(1);
        mockOrder.setTotalAmt(100.0f);
        mockOrder.setFinalAmt(100.0f);
        mockOrder.setAddress("mock address");

        List<MenuItem> menuItems = new ArrayList<>();
        MenuItem m1 = new MenuItem(1, "Item 1", 1, 1, 1);
        menuItems.add(m1);

        mockOrder.setItems(menuItems);
        mockOrder.setKilometers(2);

        LocalDateTime starTime = LocalDateTime.now();
        mockOrder.setStartTime(starTime);
        mockOrder.setExpectedTime(starTime.plusMinutes(15));
        mockOrder.setModifiedTime(starTime);
        mockOrder.setOrderStatus(OrderStatus.PLACED);
    }


    @Test
    void testUpdateOrderStatus() throws CustomException {
        when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(mockOrder);
        when(orderRepo.save(any(Order.class))).thenReturn(mockOrder);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.ACCEPTED);
        OrderResponse result = (OrderResponse) orderService.patchOrder(1, 1, orderRequest );

        assertNotNull(result);
        assertEquals(OrderStatus.ACCEPTED, result.getOrderStatus());
        verify(orderRepo, times(1)).findByUserIdAndOrderId(1, 1);
        verify(orderRepo, times(1)).save(mockOrder);
    }

    @Test
    void testUpdateOrderStatus_OrderNotFound() {
        when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(null);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.DELIVERED);
        assertThrows(CustomException.class, () -> orderService.patchOrder(1, 1, orderRequest));
        verify(orderRepo, times(1)).findByUserIdAndOrderId(1,1);
        verify(orderRepo, never()).save(any(Order.class));
    }

    @Test
    void testUpdateOrderStatus_WrongStatusUpdate() throws CustomException {
        when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(mockOrder);
        when(orderRepo.save(any(Order.class))).thenReturn(mockOrder);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);

        assertThrows(CustomException.class, () -> orderService.patchOrder(1, 1, orderRequest));
        verify(orderRepo, times(1)).findByUserIdAndOrderId(1, 1);
        verify(orderRepo, times(0)).save(mockOrder);
    }

    @Test
    void testUpdateOrderStatus_MarkOrderAsOutForDelivery_Success() throws CustomException {
        mockOrder.setOrderStatus(OrderStatus.READY_FOR_PICKUP);
        when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(mockOrder);
        when(orderRepo.save(any(Order.class))).thenReturn(mockOrder);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        orderRequest.setDeliveryPersonnelId(3);
        OrderResponse result = (OrderResponse) orderService.patchOrder(1, 1, orderRequest );

        assertNotNull(result);
        assertEquals(OrderStatus.OUT_FOR_DELIVERY, result.getOrderStatus());
        assertEquals(3, result.getDeliveryPersonnelId());
        verify(orderRepo, times(1)).findByUserIdAndOrderId(1, 1);
        verify(orderRepo, times(1)).save(mockOrder);
    }

    @Test
    void testUpdateOrderStatus_MarkOrderAsOutForDelivery_Fail() throws CustomException {
        mockOrder.setOrderStatus(OrderStatus.READY_FOR_PICKUP);
        when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(mockOrder);
        when(orderRepo.save(any(Order.class))).thenReturn(mockOrder);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        
        assertThrows(CustomException.class, () -> orderService.patchOrder(1, 1, orderRequest));
        verify(orderRepo, times(1)).findByUserIdAndOrderId(1, 1);
        verify(orderRepo, times(0)).save(mockOrder);
    }

    @Test
    void testUpdateOrderStatus_CompleteOrder() throws CustomException {
        mockOrder.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(mockOrder);
        when(orderRepo.save(any(Order.class))).thenReturn(mockOrder);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.DELIVERED);
        OrderResponseDTO result = orderService.patchOrder(1, 1, orderRequest );

        assertNotNull(result);
        assertEquals(CompletedOrderResponse.class, result.getClass());

        verify(orderRepo, times(1)).findByUserIdAndOrderId(1, 1);
        verify(orderRepo, times(1)).save(mockOrder);
    }

    @Test
    void testUpdateOrderStatus_CompleteOrderOvertime() throws CustomException {
        mockOrder.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        mockOrder.setExpectedTime(LocalDateTime.now().minusMinutes(60));
        when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(mockOrder);
        when(orderRepo.save(any(Order.class))).thenReturn(mockOrder);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderStatus(OrderStatus.DELIVERED);
        CompletedOrderResponse result = (CompletedOrderResponse) orderService.patchOrder(1, 1, orderRequest );

        assertNotNull(result);
        assertNotNull(result.getDiscountCode());

        verify(orderRepo, times(1)).findByUserIdAndOrderId(1, 1);
        verify(orderRepo, times(1)).save(mockOrder);
    }
}
