package com.food.ordering.system.restaurant.service.domain.ports.output.repository;

import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

public interface IRestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
