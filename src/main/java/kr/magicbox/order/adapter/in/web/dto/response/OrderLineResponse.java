package kr.magicbox.order.adapter.in.web.dto.response;

import kr.magicbox.order.application.dto.result.OrderLineResult;
import kr.magicbox.order.domain.enums.OrderLineDeliveryStatus;
import kr.magicbox.order.domain.enums.ProductType;
import lombok.Builder;

@Builder
public record OrderLineResponse(
        Long orderLineId,
        Long productId,
        String productName,
        int quantity,
        long unitPrice,
        ProductType productType,
        String thumbnailUrl,
        OrderLineDeliveryStatus deliveryStatus
) {
    public static OrderLineResponse from(OrderLineResult result) {
        return OrderLineResponse.builder()
                .orderLineId(result.orderLineId())
                .productId(result.productId())
                .productName(result.productName())
                .quantity(result.quantity())
                .unitPrice(result.unitPrice())
                .productType(result.productType())
                .thumbnailUrl(result.thumbnailUrl())
                .deliveryStatus(result.deliveryStatus())
                .build();
    }
}
