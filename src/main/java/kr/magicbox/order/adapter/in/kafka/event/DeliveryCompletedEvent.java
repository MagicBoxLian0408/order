package kr.magicbox.order.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record DeliveryCompletedEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("order_line_id") Long orderLineId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("delivery_id") Long deliveryId,
        @JsonProperty("tracking_number") String trackingNumber,
        @JsonProperty("delivered_at") Instant deliveredAt,
        @JsonProperty("items") List<ItemPayload> items,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
    public record ItemPayload(
            @JsonProperty("product_id") Long productId,
            @JsonProperty("quantity") int quantity
    ) {}
}
