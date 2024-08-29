package com.food.ordering.system.payment.service.domain.entity;

import com.food.ordering.system.domain.entity.ABAseEntity;
import com.food.ordering.system.domain.value.object.CustomerId;
import com.food.ordering.system.domain.value.object.Money;
import com.food.ordering.system.payment.service.domain.value.object.CreditEntryId;

public class CreditEntry extends ABAseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private final Money totalCreditAmount;

    private CreditEntry(Builder builder) {
        setId(builder.creditEntryId);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addCreditAmount(Money amount) {
        this.totalCreditAmount.add(amount);
    }

    public void subtractCreditAmount(Money amount) {
        this.totalCreditAmount.subtract(amount);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public Builder creditEntryId(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
