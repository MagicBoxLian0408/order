package kr.magicbox.order.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record PaymentCancelFailedEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("reason") String reason,
        @JsonProperty("pg_code") String pgCode,
        @JsonProperty("pg_message") String pgMessage,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {}
