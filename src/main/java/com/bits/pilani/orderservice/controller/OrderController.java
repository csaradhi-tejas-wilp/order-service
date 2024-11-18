package com.bits.pilani.orderservice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bits.pilani.orderservice.exception.CustomException;
import com.bits.pilani.orderservice.dto.OrderRequest;
import com.bits.pilani.orderservice.dto.OrderResponse;
import com.bits.pilani.orderservice.entity.Order;
import com.bits.pilani.orderservice.enums.OrderStatus;
import com.bits.pilani.orderservice.repository.OrderRepo;
import com.bits.pilani.orderservice.service.OrderService;
import com.bits.pilani.orderservice.utils.OrderConvertor;
import com.bits.pilani.orderservice.security.Authorize;
import com.bits.pilani.orderservice.security.Role;
import com.bits.pilani.orderservice.to.ResponseTO;
import com.bits.pilani.orderservice.to.SuccessResponseTO;
import com.bits.pilani.orderservice.util.TokenUtil;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderRepo orderRepo;

    @Operation(summary = "Place a new order")
    @Authorize( roles= {Role.CUSTOMER})
    @PostMapping("/order")
    public ResponseEntity<ResponseTO> placeOrder(@RequestBody OrderRequest orderRequest,
                                                    @RequestHeader("Authorization") String token) throws Exception 
    {
        int userId = TokenUtil.getUserIdFromToken(token);

        return SuccessResponseTO.create(orderService.placeOrder(orderRequest, userId), HttpStatus.CREATED);
    }

    @Operation(summary = "Get an order based on orderId")
    @Authorize( roles= {Role.CUSTOMER, Role.ADMIN, Role.DELIVERY_PERSONNEL})
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseTO> getOrder(@PathVariable int orderId,
                                                @RequestHeader("Authorization") String token) throws CustomException{

        int userId = TokenUtil.getUserIdFromToken(token);
        Order order = orderRepo.findByUserIdAndOrderId(userId, orderId);
        if(order != null){

            OrderResponse orderResponse = OrderConvertor.toOrderResponse(order);
            return SuccessResponseTO.create(orderResponse);
        }

        throw new CustomException(HttpStatus.NOT_FOUND, "Order not found!");
        
    }

    @Operation(summary = "Update orderStatus")
    @Authorize( roles= {Role.CUSTOMER, Role.RESTAURANT_OWNER, Role.ADMIN, Role.DELIVERY_PERSONNEL})
    @PatchMapping("order/{orderId}")
    public ResponseEntity<ResponseTO> updateOrder(@PathVariable int orderId, 
                                @RequestBody OrderRequest orderRequest,
                                @RequestHeader("Authorization") String token) throws Exception{

        int userId = TokenUtil.getUserIdFromToken(token);
        return SuccessResponseTO.create(orderService.patchOrder(orderId, userId, orderRequest));
    }

    @Operation(summary = "Get all orders")
    @Authorize(roles = {Role.CUSTOMER, Role.ADMIN, Role.RESTAURANT_OWNER, Role.DELIVERY_PERSONNEL})
    @GetMapping("/orders")
    public ResponseEntity<ResponseTO> getOrders(@RequestHeader("Authorization") String token,
                                                @RequestParam(name = "orderStatus", required = false) String orderStatus
                                                ) throws Exception{

        int userId = TokenUtil.getUserIdFromToken(token);

        Role role = TokenUtil.getRoleFromToken(token);
        List<Order> orders = new ArrayList<>();
        if (orderStatus != null) {
            if(role.equals(Role.ADMIN)) {
                orders = orderRepo.findAllByOrderStatus(OrderStatus.valueOf(orderStatus));
            }
            orders = orderRepo.findByUserIdAndOrderStatus(userId, OrderStatus.valueOf(orderStatus.toUpperCase()));
        } else {
            if(role.equals(Role.ADMIN)) {
                orders = orderRepo.findAll();
            }
            orders = orderRepo.findAllByUserId(userId);
        }

        List<OrderResponse> orderResponses = orders.stream().map( order -> 
                                                OrderConvertor.toOrderResponse(order)
                                            ).collect(Collectors.toList());
        
        return SuccessResponseTO.create(orderResponses);
    }
}
