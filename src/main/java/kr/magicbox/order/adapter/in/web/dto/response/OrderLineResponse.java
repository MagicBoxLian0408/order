package kr.magicbox.order.adapter.in.web.dto.response;

import kr.magicbox.order.application.dto.result.OrderLineResult;
import lombok.Builder;

@Builder
public record OrderLineResponse(
        Long orderLineId,
        Long productId,
        String productName,
        int quantity,
        long unitPrice,
        String thumbnailUrl
) {
    public static OrderLineResponse from(OrderLineResult result) {
        return OrderLineResponse.builder()
                .orderLineId(result.orderLineId())
                .productId(result.productId())
                .productName(result.productName())
                .quantity(result.quantity())
                .unitPrice(result.unitPrice())
                .thumbnailUrl(result.thumbnailUrl())
                .build();
    }
}
