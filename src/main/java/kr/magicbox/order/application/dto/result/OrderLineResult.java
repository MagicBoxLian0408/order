package kr.magicbox.order.application.dto.result;

import lombok.Builder;

@Builder
public record OrderLineResult(
        Long orderLineId,
        Long productId,
        String productName,
        int quantity,
        long unitPrice
) {}
