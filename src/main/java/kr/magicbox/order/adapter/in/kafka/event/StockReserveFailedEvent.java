package kr.magicbox.order.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record StockReserveFailedEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("reason") String reason,
        @JsonProperty("failed_items") List<FailedItemPayload> failedItems,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
    public record FailedItemPayload(
            @JsonProperty("product_id") Long productId,
            @JsonProperty("requested") int requested,
            @JsonProperty("available") int available
    ) {}
}
