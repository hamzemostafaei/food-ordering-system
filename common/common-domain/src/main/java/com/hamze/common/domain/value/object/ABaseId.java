package com.hamze.common.domain.value.object;

import java.util.Objects;

public abstract class ABaseId<T> {

    private final T value;

    protected ABaseId(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ABaseId<?> aBaseId)) return false;
        return Objects.equals(value, aBaseId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
