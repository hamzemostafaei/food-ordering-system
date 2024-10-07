package com.food.ordering.system.customer.service.data.access.customer.repository;

import com.food.ordering.system.customer.service.data.access.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerJpaRepository extends JpaRepository<CustomerEntity, String> {
}
