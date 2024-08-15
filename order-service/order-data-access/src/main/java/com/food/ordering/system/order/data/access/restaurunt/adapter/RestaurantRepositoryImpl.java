package com.food.ordering.system.order.data.access.restaurunt.adapter;

import com.food.ordering.system.order.data.access.restaurunt.entity.RestaurantEntity;
import com.food.ordering.system.order.data.access.restaurunt.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.data.access.restaurunt.repository.IRestaurantJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements IRestaurantRepository {

    private final IRestaurantJpaRepository restaurantJpaRepository;
    final private RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);

        Optional<List<RestaurantEntity>> restaurantEntities =
                restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);

        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
