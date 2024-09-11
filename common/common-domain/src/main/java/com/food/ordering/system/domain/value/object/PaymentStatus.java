package com.food.ordering.system.domain.value.object;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum PaymentStatus {
    Completed(0, "COMPLETED"),
    Cancelled(1, "CANCELLED"),
    Failed(2, "FAILED");

    private static final Map<Integer, PaymentStatus> VALUE_MAP = new HashMap<>();
    private static final Map<String, PaymentStatus> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (PaymentStatus value : PaymentStatus.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    PaymentStatus(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static PaymentStatus getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        PaymentStatus value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static PaymentStatus getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        PaymentStatus value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }
}
