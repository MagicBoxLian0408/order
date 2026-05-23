package kr.magicbox.order.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record OrderPrepareEventDto(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("seller_id") Long sellerId,
        @JsonProperty("items") List<ItemPayload> items,
        @JsonProperty("total_amount") long totalAmount,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
    public record ItemPayload(
            @JsonProperty("product_id") Long productId,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("unit_price") long unitPrice,
            @JsonProperty("product_name") String productName
    ) {}
}
