package kr.magicbox.order.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.order.domain.aggregate.Order;
import lombok.Builder;

import java.time.Instant;

@Builder
public record OrderConfirmedEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("seller_id") Long sellerId,
        @JsonProperty("confirmed_at") Instant confirmedAt,
        @JsonProperty("occurred_at") Instant occurredAt
) implements OrderDomainEvent {

    public static OrderConfirmedEvent from(Order order) {
        Instant now = Instant.now();
        Long orderId = order.getId().value();
        return OrderConfirmedEvent.builder()
                .eventId(orderId)
                .orderId(orderId)
                .customerId(order.getCustomerId())
                .sellerId(order.getSellerId())
                .confirmedAt(now)
                .occurredAt(now)
                .build();
    }

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public OrderDomainEventType eventType() {
        return OrderDomainEventType.ORDER_CONFIRMED;
    }
}
