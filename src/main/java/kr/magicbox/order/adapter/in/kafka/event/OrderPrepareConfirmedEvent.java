package kr.magicbox.order.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record OrderPrepareConfirmedEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {}
