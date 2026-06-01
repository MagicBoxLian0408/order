package kr.magicbox.order.application.dto.result;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateOrderResult(
        Long orderId,
        Long sellerId,
        Long totalAmount,
        List<OrderLineResult> orderLines
) {}
