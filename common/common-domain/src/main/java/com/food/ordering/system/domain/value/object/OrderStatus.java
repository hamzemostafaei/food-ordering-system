package com.food.ordering.system.domain.value.object;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {

    Pending(0, "PENDING"),
    Paid(1, "PAID"),
    Approved(2, "APPROVED"),
    Cancelling(3, "CANCELLING"),
    Cancelled(4, "CANCELLED");

    private static final Map<Integer, OrderStatus> VALUE_MAP = new HashMap<>();
    private static final Map<String, OrderStatus> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (OrderStatus value : OrderStatus.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    OrderStatus(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static OrderStatus getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        OrderStatus value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static OrderStatus getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        OrderStatus value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }

}
