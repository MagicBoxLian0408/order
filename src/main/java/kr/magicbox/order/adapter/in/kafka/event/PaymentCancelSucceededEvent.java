package kr.magicbox.order.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record PaymentCancelSucceededEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("pg_transaction_id") String pgTransactionId,
        @JsonProperty("refund_amount") long refundAmount,
        @JsonProperty("currency") String currency,
        @JsonProperty("refunded_at") Instant refundedAt,
        @JsonProperty("items") List<ItemPayload> items,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {
    public record ItemPayload(
            @JsonProperty("product_id") Long productId,
            @JsonProperty("quantity") int quantity
    ) {}
}
