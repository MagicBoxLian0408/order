package kr.magicbox.order.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.enums.ProductType;
import kr.magicbox.order.domain.vo.ShippingAddress;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record OrderPrepareEvent(
        @JsonProperty("event_id") Long eventId,
        @JsonProperty("order_id") Long orderId,
        @JsonProperty("customer_id") Long customerId,
        @JsonProperty("seller_id") Long sellerId,
        @JsonProperty("items") List<OrderItemPayload> items,
        @JsonProperty("total_amount") long totalAmount,
        @JsonProperty("shipping_address") ShippingAddressPayload shippingAddress,
        @JsonProperty("occurred_at") Instant occurredAt
) implements OrderDomainEvent {

    public static OrderPrepareEvent from(Order order) {
        Long orderId = order.getId().value();
        List<OrderItemPayload> items = order.getOrderLines().stream()
                .map(line -> OrderItemPayload.builder()
                        .orderLineId(line.getId().value())
                        .productId(line.getProductId())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .productName(line.getProductName())
                        .productType(line.getProductType())
                        .build())
                .toList();

        ShippingAddress addr = order.getShippingAddress();
        ShippingAddressPayload shippingAddress = ShippingAddressPayload.builder()
                .recipient(addr.recipient())
                .phone(addr.phone())
                .zipCode(addr.zipCode())
                .address1(addr.address1())
                .address2(addr.address2())
                .build();

        return OrderPrepareEvent.builder()
                .eventId(orderId)
                .orderId(orderId)
                .customerId(order.getCustomerId())
                .sellerId(order.getSellerId())
                .items(items)
                .totalAmount(order.getTotalAmount())
                .shippingAddress(shippingAddress)
                .occurredAt(Instant.now())
                .build();
    }

    @Override
    public String key() {
        return orderId.toString();
    }

    @Override
    public OrderDomainEventType eventType() {
        return OrderDomainEventType.ORDER_PREPARE;
    }

    @Builder
    public record OrderItemPayload(
            @JsonProperty("order_line_id") Long orderLineId,
            @JsonProperty("product_id") Long productId,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("unit_price") long unitPrice,
            @JsonProperty("product_name") String productName,
            @JsonProperty("product_type") ProductType productType
    ) {}

    @Builder
    public record ShippingAddressPayload(
            @JsonProperty("recipient") String recipient,
            @JsonProperty("phone") String phone,
            @JsonProperty("zip_code") String zipCode,
            @JsonProperty("address1") String address1,
            @JsonProperty("address2") String address2
    ) {}
}
