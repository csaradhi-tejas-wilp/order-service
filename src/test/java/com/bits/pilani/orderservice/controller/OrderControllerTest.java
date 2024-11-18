package com.bits.pilani.orderservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bits.pilani.orderservice.dto.MenuItem;
import com.bits.pilani.orderservice.dto.OrderRequest;
import com.bits.pilani.orderservice.dto.OrderResponse;
import com.bits.pilani.orderservice.entity.Order;
import com.bits.pilani.orderservice.enums.OrderStatus;
import com.bits.pilani.orderservice.repository.OrderRepo;
import com.bits.pilani.orderservice.service.OrderService;
import com.bits.pilani.orderservice.security.JwtAuthHandlerInterceptor;
import com.bits.pilani.orderservice.util.TokenUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    OrderRepo orderRepo;

    Order mockOrder;
    
    OrderResponse mockOrderResponse;

    @MockBean
    JwtAuthHandlerInterceptor jwtAuthHandlerInterceptor;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmhpY3YiLCJpYXQiOjE3MzE1NzgxOTcsInVzZXJJZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0.Jf6booIahu5FRNlZGeJZRyt2THrqOmXaVW7ocVE_c0Q";

    private MockHttpServletRequestBuilder withAuth(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", AUTH_TOKEN);
    }

    @Mock
    TokenUtil tokenUtil;

    @BeforeEach
    void setUp() throws Exception {
        
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

        mockOrderResponse = new OrderResponse();

        mockOrderResponse.setOrderId(1);
        mockOrderResponse.setRestaurantId(1);
        mockOrderResponse.setTotalAmt(100.0f);
        mockOrderResponse.setFinalAmt(100.0f);
        mockOrderResponse.setAddress("mock address");

        mockOrderResponse.setItems(menuItems);
        mockOrderResponse.setKilometers(2);
        mockOrderResponse.setStartTime(starTime);
        mockOrderResponse.setExpectedTime(starTime.plusMinutes(15));
        mockOrderResponse.setModifiedTime(starTime);
        mockOrderResponse.setOrderStatus(OrderStatus.PLACED);

        when(jwtAuthHandlerInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
    }

    @Test
    void testCreateOrder() throws Exception {
        //OrderResponse mockResponse = new OrderResponse(1, "Order Created Successfully", null);
        Mockito.when(orderService.placeOrder(any(OrderRequest.class), Mockito.anyInt())).thenReturn(mockOrderResponse);

        mockMvc.perform(withAuth(post("/order"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\r\n" + //
                                        "    \"restaurantId\": 1,\r\n" + //
                                        "    \"items\": [\r\n" + //
                                        "        {\r\n" + //
                                        "            \"id\": 1,\r\n" + //
                                        "            \"name\": \"Margherita Pizza\",\r\n" + //
                                        "            \"categoryId\": \"1\",\r\n" + //
                                        "            \"cuisineId\" : \"1\",\r\n" + //
                                        "            \"quantity\": 2\r\n" + //
                                        "        },\r\n" + //
                                        "        {\r\n" + //
                                        "            \"id\": 2,\r\n" + //
                                        "            \"name\": \"Pasta Carbonara\",\r\n" + //
                                        "            \"categoryId\": \"2\",\r\n" + //
                                        "            \"cuisineId\" : \"2\",\r\n" + //
                                        "            \"quantity\": 1\r\n" + //
                                        "        }\r\n" + //
                                        "    ],\r\n" + //
                                        "    \"totalAmt\": 31.0,\r\n" + //
                                        "    \"restaurantDiscId\": \"DISC10\",\r\n" + //
                                        "    \"restaurantDiscAmt\": 10.0,\r\n" + //
                                        "    \"finalAmt\": 21.5,\r\n" + //
                                        "    \"orderStatus\": \"PLACED\",\r\n" + //
                                        "    \"startTime\": \"2024-11-14T19:57:16\",\r\n" + //
                                        "    \"endTime\": null,\r\n" + //
                                        "    \"expectedTime\": \"2024-11-14T20:37:16\",\r\n" + //
                                        "    \"address\": \"15 Lotus Street, Sector 21, Noida, Uttar Pradesh 201301, India\",\r\n" + //
                                        "    \"kilometers\": 7\r\n" + //
                                        "}")
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.orderId").value("1"));
    }

    @Test
    void testGetOrderById() throws Exception {
        Mockito.when(orderRepo.findByUserIdAndOrderId(1,1)).thenReturn(mockOrder);

        mockMvc.perform(withAuth(get("/order/1"))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value("1"));
    }

    @Test
    void testGetAllOrders() throws Exception {
        Mockito.when(orderRepo.findByUserIdAndOrderStatus(Mockito.anyInt(), any(OrderStatus.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(withAuth(get("/orders"))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        Mockito.when(orderService.patchOrder(anyInt(), anyInt(), any(OrderRequest.class))).thenReturn(mockOrderResponse);

        mockMvc.perform(withAuth(patch("/order/1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"orderStatus\":\"DELIVERED\"}")
        )
                .andExpect(status().isOk());
    }
}


