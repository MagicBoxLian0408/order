package kr.magicbox.order.adapter.in.web.dto.response;

import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.domain.enums.OrderStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record OrderResponse(
        Long orderId,
        Long customerId,
        Long sellerId,
        OrderStatus status,
        long totalAmount,
        ShippingAddressResponse shippingAddress,
        List<OrderLineResponse> orderLines,
        Instant createdAt
) {
    public static OrderResponse from(OrderResult result) {
        List<OrderLineResponse> lineResponses = result.orderLines().stream()
                .map(OrderLineResponse::from)
                .toList();

        return OrderResponse.builder()
                .orderId(result.orderId())
                .customerId(result.customerId())
                .sellerId(result.sellerId())
                .status(result.status())
                .totalAmount(result.totalAmount())
                .shippingAddress(ShippingAddressResponse.from(result.shippingAddress()))
                .orderLines(lineResponses)
                .createdAt(result.createdAt())
                .build();
    }
}

