package kr.magicbox.order.application.dto.command;

import kr.magicbox.order.domain.vo.ShippingAddress;
import lombok.Builder;

@Builder
public record CreateReleaseOrderCommand(
        Long customerId,
        Long sellerId,
        Long releaseId,
        String purchaseToken,
        String productName,
        Long unitPrice,
        ShippingAddress shippingAddress
) {}
