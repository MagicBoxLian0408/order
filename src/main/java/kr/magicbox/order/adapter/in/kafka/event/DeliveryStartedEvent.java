package kr.magicbox.order.adapter.in.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record DeliveryStartedEvent(
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("order_line_id") Long orderLineId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("delivery_id") Long deliveryId,
        @JsonProperty("carrier") String carrier,
        @JsonProperty("carrier_code") String carrierCode,
        @JsonProperty("tracking_number") String trackingNumber,
        @JsonProperty("dispatched_at") Instant dispatchedAt,
        @JsonProperty("occurred_at") Instant occurredAt
) implements InboxEvent {}
