package com.bits.pilani.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bits.pilani.orderservice.exception.CustomException;
import com.bits.pilani.orderservice.repository.OrderDetailsRepo;
import com.bits.pilani.orderservice.service.OrderDetailsService;
import com.bits.pilani.orderservice.to.ResponseTO;
import com.bits.pilani.orderservice.to.SuccessResponseTO;
import com.bits.pilani.orderservice.util.TokenUtil;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/order/details")
public class OrderDetailsController {

    @Autowired
    OrderDetailsService orderDetailsService;

    @Autowired
    OrderDetailsRepo orderDetailsRepo;

    @Operation(summary = "Get a list of popular items based on restaurant and/or cuisine")
    @GetMapping("/popular/item")
    public ResponseEntity<ResponseTO> getMostOrderedItems(@RequestParam(required = false) String cuisineId,
                                                            @RequestParam(required = false) String restaurantId) throws CustomException {
        
        if(cuisineId != null || restaurantId != null) {
            if(cuisineId == null) {
                return SuccessResponseTO.create(orderDetailsService.toPopularItemResponse(orderDetailsRepo.findMostOrderedItemsByRestaurantId(Integer.parseInt(restaurantId))));
            } else if(restaurantId == null) {
                return SuccessResponseTO.create(orderDetailsService.toPopularItemResponse(orderDetailsRepo.findMostOrderedItemsByCuisineId(Integer.parseInt(cuisineId))));
            }

            return SuccessResponseTO.create(orderDetailsService.toPopularItemResponse(orderDetailsRepo.findMostOrderedItemsByRestaurantIdAndCuisineId(Integer.parseInt(restaurantId), Integer.parseInt(cuisineId))));
        }
            
        throw new CustomException(HttpStatus.BAD_REQUEST, "Please specify restaurantId or cuisineId");
    }

    @Operation(summary = "Get a list of popular restaurants with/without cuisine filter")
    @GetMapping("/popular/restaurant")
    public ResponseEntity<ResponseTO> getMostPopularRestaurants(@RequestParam(required = false) String cuisineId)
    {
        if(cuisineId == null) {
            return SuccessResponseTO.create(orderDetailsService.toPopularRestaurantResponse(orderDetailsRepo.findTop5RestaurantsByOrders()));
        }

        return SuccessResponseTO.create(orderDetailsService.toPopularRestaurantResponse(orderDetailsRepo.findTopRestaurantsByCuisineId(Integer.parseInt(cuisineId))));
        
    }

    @Operation(summary = "Get a list of items I ordered the most based on restaurant and/or cuisine")
    @GetMapping("/popular/item/personal")
    public ResponseEntity<ResponseTO> getMostOrderedItems( @RequestHeader("Authorization") String token,
                                                            @RequestParam(required = false) String cuisineId,
                                                            @RequestParam(required = false) String restaurantId) throws CustomException {
        
        int userId = TokenUtil.getUserIdFromToken(token);
        if(cuisineId != null || restaurantId != null) {
            if(cuisineId == null) {
                return SuccessResponseTO.create(orderDetailsService.toPopularItemResponse(orderDetailsRepo.findMostOrderedItemsByUserAndRestaurantId(userId, Integer.parseInt(restaurantId))));
            } else if(restaurantId == null) {
                return SuccessResponseTO.create(orderDetailsService.toPopularItemResponse(orderDetailsRepo.findMostOrderedItemsByUserAndCuisineId(userId, Integer.parseInt(cuisineId))));
            }

            return SuccessResponseTO.create(orderDetailsService.toPopularItemResponse(orderDetailsRepo.findMostOrderedItemsByUserAndRestaurantIdAndCuisineId(userId, Integer.parseInt(restaurantId), Integer.parseInt(cuisineId))));
        }
            
        throw new CustomException(HttpStatus.BAD_REQUEST, "Please specify restaurantId or cuisineId");
    }

    @Operation(summary = "Get a list of restaurants I ordered the most from with/without cuisine filter")
    @GetMapping("/popular/restaurant/personal")
    public ResponseEntity<ResponseTO> getMostPopularRestaurants(@RequestHeader("Authorization") String token,
                                                                @RequestParam(required = false) String cuisineId) throws CustomException {
        
        int userId = TokenUtil.getUserIdFromToken(token);
        if(cuisineId == null) {
            return SuccessResponseTO.create(orderDetailsService.toPopularRestaurantResponse(orderDetailsRepo.findTop5RestaurantsByOrdersAndUserId(userId)));
        }

        return SuccessResponseTO.create(orderDetailsService.toPopularRestaurantResponse(orderDetailsRepo.findTopRestaurantsByUserIdAndCuisineId(Integer.parseInt(cuisineId), userId)));
        
    }
}
