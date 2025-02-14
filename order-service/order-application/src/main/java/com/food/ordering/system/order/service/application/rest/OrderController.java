package com.food.ordering.system.order.service.application.rest;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.servive.IOrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final IOrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand) {

        if (log.isInfoEnabled()) {
            log.info("Creating order for customer: [{}] at restaurant: [{}]",
                    createOrderCommand.getCustomerId(),
                    createOrderCommand.getRestaurantId()
            );
        }

        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);

        if (log.isInfoEnabled()) {
            log.info("Order created with tracking id: [{}]", createOrderResponse.getOrderTrackingId());
        }

        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable String trackingId) {
        TrackOrderResponse trackOrderResponse =
                orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());

        if (log.isInfoEnabled()) {
            log.info("Returning order status with tracking id: [{}]", trackOrderResponse.getOrderTrackingId());
        }

        return ResponseEntity.ok(trackOrderResponse);
    }
}