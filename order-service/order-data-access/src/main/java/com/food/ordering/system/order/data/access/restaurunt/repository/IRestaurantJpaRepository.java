package com.food.ordering.system.order.data.access.restaurunt.repository;

import com.food.ordering.system.order.data.access.restaurunt.entity.RestaurantEntity;
import com.food.ordering.system.order.data.access.restaurunt.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    @Query("""
            select r from RestaurantEntity r
            where r.id.restaurantId = :restaurantId
            and r.id.productId in :productIds
            """)
    Optional<List<RestaurantEntity>> findByRestaurantIdAndProductIdIn(String restaurantId, List<String> productIds);
}
