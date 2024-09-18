package com.food.ordering.system.outbox;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum OutboxStatus {

    Started(0, "STARTED"),
    Completed(1, "COMPLETED"),
    Failed(1, "FAILED");

    private static final Map<Integer, OutboxStatus> VALUE_MAP = new HashMap<>();
    private static final Map<String, OutboxStatus> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (OutboxStatus value : OutboxStatus.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    OutboxStatus(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static OutboxStatus getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        OutboxStatus value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static OutboxStatus getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        OutboxStatus value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }
}
