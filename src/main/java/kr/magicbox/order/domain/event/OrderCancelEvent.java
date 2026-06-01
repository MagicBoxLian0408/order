package kr.magicbox.order.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.order.domain.aggregate.Order;
import lombok.Builder;

import java.time.Instant;

@Builder
public record OrderCancelEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("reason") String reason,
        @JsonProperty("requested_at") Instant requestedAt,
        @JsonProperty("occurred_at") Instant occurredAt
) implements OrderDomainEvent {

    public static OrderCancelEvent from(Order order, String reason) {
        Instant now = Instant.now();
        Long orderId = order.getId().value();
        return OrderCancelEvent.builder()
                .eventId(orderId)
                .orderId(orderId)
                .customerId(order.getCustomerId())
                .reason(reason)
                .requestedAt(now)
                .occurredAt(now)
                .build();
    }

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public OrderDomainEventType eventType() {
        return OrderDomainEventType.ORDER_CANCEL;
    }
}
