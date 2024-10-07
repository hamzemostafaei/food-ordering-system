package com.food.ordering.system.customer.service.data.access.customer.mapper;

import com.food.ordering.system.customer.service.data.access.customer.entity.CustomerEntity;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.value.object.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {
    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()),
                customerEntity.getUsername(),
                customerEntity.getFirstName(),
                customerEntity.getLastName());
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}