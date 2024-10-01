package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.ABaseOrderApprovalEvent;

import java.util.List;

public interface IRestaurantDomainService {
    ABaseOrderApprovalEvent validateOrder(Restaurant restaurant, List<String> failureMessages);
}
