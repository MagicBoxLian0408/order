package kr.magicbox.order.application.dto.result;

import kr.magicbox.order.domain.enums.ProductType;
import lombok.Builder;

@Builder
public record OrderLineResult(
        Long orderLineId,
        Long productId,
        String productName,
        int quantity,
        long unitPrice,
        ProductType productType,
        String thumbnailUrl
) {}
