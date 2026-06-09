package kr.magicbox.order.adapter.in.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.magicbox.order.application.dto.command.CreateOrderCommand;

import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "판매자 ID는 필수입니다.") @Positive(message = "판매자 ID는 양수여야 합니다.") Long sellerId,
        @NotNull(message = "총 금액은 필수입니다.") @Positive(message = "총 금액은 양수여야 합니다.") Long totalAmount,
        @Valid @NotNull(message = "배송지 정보는 필수입니다.") ShippingAddressRequest shippingAddress,
        @NotEmpty(message = "주문 항목은 하나 이상이어야 합니다.") List<@Valid OrderLineRequest> orderLines
) {
    public CreateOrderCommand toCommand(Long customerId) {
        List<CreateOrderCommand.OrderLineCommand> lineCommands = orderLines.stream()
                .map(line -> CreateOrderCommand.OrderLineCommand.builder()
                        .productId(line.productId())
                        .sellerId(line.sellerId())
                        .productName(line.productName())
                        .quantity(line.quantity())
                        .unitPrice(line.unitPrice())
                        .productType(line.productType())
                        .build())
                .toList();

        return CreateOrderCommand.builder()
                .customerId(customerId)
                .sellerId(sellerId)
                .totalAmount(totalAmount)
                .shippingAddress(shippingAddress.toDomain())
                .orderLines(lineCommands)
                .build();
    }
}
