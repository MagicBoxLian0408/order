package kr.magicbox.order.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.aggregate.OrderLine;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record OrderPurchaseConfirmedEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("order_line_id") Long orderLineId,
        @JsonProperty("seller_id") Long sellerId,
        @JsonProperty("gross_amount") long grossAmount,
        @JsonProperty("occurred_at") Instant occurredAt
) implements OrderDomainEvent {

    public static OrderPurchaseConfirmedEvent from(Order order, OrderLine line, Instant now) {
        Long orderLineId = line.getId().value();
        return OrderPurchaseConfirmedEvent.builder()
                .eventId(orderLineId)
                .orderId(order.getId().value())
                .customerId(order.getCustomerId())
                .orderLineId(orderLineId)
                .sellerId(line.getSellerId())
                .grossAmount(line.lineTotal())
                .occurredAt(now)
                .build();
    }

    public static List<OrderPurchaseConfirmedEvent> fromShippedLines(Order order) {
        Instant now = Instant.now();
        return order.shippedLines().stream()
                .map(line -> from(order, line, now))
                .toList();
    }

    @Override
    public String key() {
        return orderLineId.toString();
    }

    @Override
    public OrderDomainEventType eventType() {
        return OrderDomainEventType.ORDER_PURCHASE_CONFIRMED;
    }
}
