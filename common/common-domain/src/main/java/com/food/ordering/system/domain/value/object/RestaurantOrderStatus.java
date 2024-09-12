package com.food.ordering.system.domain.value.object;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum RestaurantOrderStatus {
    Paid(0, "PAID");

    private static final Map<Integer, RestaurantOrderStatus> VALUE_MAP = new HashMap<>();
    private static final Map<String, RestaurantOrderStatus> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (RestaurantOrderStatus value : RestaurantOrderStatus.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    RestaurantOrderStatus(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static RestaurantOrderStatus getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        RestaurantOrderStatus value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static RestaurantOrderStatus getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        RestaurantOrderStatus value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }
}
