package com.food.ordering.system.order.service.domain.dto.track;

import com.food.ordering.system.domain.value.object.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class TrackOrderResponse {
    private final String orderTrackingId;
    private final OrderStatus orderStatus;
    private final List<String> failureMessages;
}
