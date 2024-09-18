package com.food.ordering.system.saga;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum SagaStatus {
    Started(0, "STARTED"),
    Failed(1, "FAILED"),
    Succeeded(2, "SUCCEEDED"),
    Processing(3, "PROCESSING"),
    Compensating(4, "COMPENSATING"),
    Compensated(5, "COMPENSATED");

    private static final Map<Integer, SagaStatus> VALUE_MAP = new HashMap<>();
    private static final Map<String, SagaStatus> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (SagaStatus value : SagaStatus.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    SagaStatus(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static SagaStatus getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        SagaStatus value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static SagaStatus getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        SagaStatus value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }
}
