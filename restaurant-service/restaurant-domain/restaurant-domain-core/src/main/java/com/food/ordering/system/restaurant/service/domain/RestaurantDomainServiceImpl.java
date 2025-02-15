package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.value.object.OrderApprovalStatus;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.ABaseOrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordering.system.restaurant.service.domain.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j
public class RestaurantDomainServiceImpl implements IRestaurantDomainService {

    @Override
    public ABaseOrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages) {
        restaurant.validateOrder(failureMessages);
        if (log.isInfoEnabled()) {
            log.info("Validating order with id: {}", restaurant.getOrderDetail().getId().getValue());
        }

        if (failureMessages.isEmpty()) {
            if (log.isInfoEnabled()) {
                log.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue());
            }
            restaurant.constructOrderApproval(OrderApprovalStatus.Approved);
            return new OrderApprovedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        } else {
            if (log.isInfoEnabled()) {
                log.info("Order is rejected for order id: {}", restaurant.getOrderDetail().getId().getValue());
            }
            restaurant.constructOrderApproval(OrderApprovalStatus.Rejected);
            return new OrderRejectedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(UTC)));
        }
    }
}
