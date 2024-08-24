package com.food.ordering.system.order.data.access.restaurunt.mapper;

import com.food.ordering.system.domain.value.object.Money;
import com.food.ordering.system.domain.value.object.ProductId;
import com.food.ordering.system.domain.value.object.RestaurantId;
import com.food.ordering.system.order.data.access.restaurunt.entity.RestaurantEntity;
import com.food.ordering.system.order.data.access.restaurunt.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
        return restaurant.getProducts()
                .stream()
                .map(product -> product.getId().getValue())
                .toList();
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
        RestaurantEntity restaurantEntity = restaurantEntities.stream()
                .findFirst()
                .orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));

        List<Product> restaurantProducts = restaurantEntities.stream()
                .map(entity ->
                        new Product(
                                new ProductId(entity.getId().getProductId()), entity.getProductName(), new Money(entity.getProductPrice()))
                ).toList();

        return Restaurant.builder()
                .restaurantId(new RestaurantId(restaurantEntity.getId().getRestaurantId()))
                .products(restaurantProducts)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}