package kr.magicbox.order.application.dto.result;

import kr.magicbox.order.domain.enums.OrderStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record OrderResult(
        Long orderId,
        Long customerId,
        Long sellerId,
        OrderStatus status,
        long totalAmount,
        ShippingAddressResult shippingAddress,
        List<OrderLineResult> orderLines,
        Instant createdAt
) {}
