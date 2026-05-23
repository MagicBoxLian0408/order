package kr.magicbox.order.application.dto.command;

import kr.magicbox.order.domain.vo.ShippingAddress;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateOrderCommand(
        Long customerId,
        Long sellerId,
        Long totalAmount,
        ShippingAddress shippingAddress,
        List<OrderLineCommand> orderLines
) {
    @Builder
    public record OrderLineCommand(
            Long productId,
            Long sellerId,
            String productName,
            Integer quantity,
            Long unitPrice
    ) {}
}
