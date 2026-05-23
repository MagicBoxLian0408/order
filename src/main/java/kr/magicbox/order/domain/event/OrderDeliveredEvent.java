package kr.magicbox.order.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.order.domain.aggregate.Order;
import lombok.Builder;

import java.time.Instant;

@Builder
public record OrderDeliveredEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("delivered_at") Instant deliveredAt,
        @JsonProperty("occurred_at") Instant occurredAt
) implements OrderDomainEvent {

    public static OrderDeliveredEvent from(Order order) {
        Instant now = Instant.now();
        return OrderDeliveredEvent.builder()
                .orderId(order.getId().value())
                .customerId(order.getCustomerId())
                .deliveredAt(now)
                .occurredAt(now)
                .build();
    }

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public OrderDomainEventType eventType() {
        return OrderDomainEventType.ORDER_DELIVERED;
    }
}
