package com.food.ordering.system.order.service.domain.value.object;

import java.util.Objects;

public class StreetAddress {
    protected final String city;
    private final Long id;
    private final String street;
    private final String postalCode;

    public StreetAddress(Long id, String street, String postalCode, String city) {
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StreetAddress that)) return false;
        return Objects.equals(city, that.city) && Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, postalCode);
    }
}
