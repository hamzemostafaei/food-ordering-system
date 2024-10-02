package com.food.ordering.system.payment.service.domain.value.object;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum TransactionType {
    Debit(0, "DEBIT"),
    Credit(1, "CREDIT");

    private static final Map<Integer, TransactionType> VALUE_MAP = new HashMap<>();
    private static final Map<String, TransactionType> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (TransactionType value : TransactionType.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    TransactionType(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static TransactionType getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        TransactionType value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static TransactionType getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        TransactionType value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }
}
