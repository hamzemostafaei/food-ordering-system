package com.food.ordering.system.domain.value.object;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum PaymentOrderStatus {

    Pending(0, "PENDING"),
    Cancelled(1, "CANCELLED");

    private static final Map<Integer, PaymentOrderStatus> VALUE_MAP = new HashMap<>();
    private static final Map<String, PaymentOrderStatus> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (PaymentOrderStatus value : PaymentOrderStatus.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    PaymentOrderStatus(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static PaymentOrderStatus getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        PaymentOrderStatus value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static PaymentOrderStatus getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        PaymentOrderStatus value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }
}
