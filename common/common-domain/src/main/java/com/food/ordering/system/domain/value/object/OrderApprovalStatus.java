package com.food.ordering.system.domain.value.object;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum OrderApprovalStatus {
    Approved(0, "APPROVED"),
    Rejected(1, "REJECTED"),;

    private static final Map<Integer, OrderApprovalStatus> VALUE_MAP = new HashMap<>();
    private static final Map<String, OrderApprovalStatus> VALUE_NAME_MAP = new HashMap<>();

    static {
        for (OrderApprovalStatus value : OrderApprovalStatus.values()) {
            VALUE_MAP.put(value.getTypeCode(), value);
            VALUE_NAME_MAP.put(value.getName(), value);
        }
    }

    @Getter
    private final int typeCode;
    @Getter
    private final String name;

    OrderApprovalStatus(int typeCode, String name) {
        this.typeCode = typeCode;
        this.name = name;
    }

    public static OrderApprovalStatus getByValue(Integer typeCode) {
        if (typeCode == null) {
            return null;
        }

        OrderApprovalStatus value = VALUE_MAP.get(typeCode);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeCode + "] is provided.");
        }

        return value;
    }

    public static OrderApprovalStatus getByName(String typeName) {
        if (typeName == null) {
            return null;
        }

        OrderApprovalStatus value = VALUE_NAME_MAP.get(typeName);

        if (value == null) {
            throw new IllegalArgumentException("Bad typeCode code [" + typeName + "] is provided.");
        }

        return value;
    }
}
