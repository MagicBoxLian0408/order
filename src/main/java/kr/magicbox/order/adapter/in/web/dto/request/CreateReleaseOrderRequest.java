package kr.magicbox.order.adapter.in.web.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.magicbox.order.application.dto.command.CreateReleaseOrderCommand;

public record CreateReleaseOrderRequest(
        @NotNull(message = "판매자 ID는 필수입니다.") @Positive(message = "판매자 ID는 양수여야 합니다.") Long sellerId,
        @NotNull(message = "릴리즈 ID는 필수입니다.") @Positive(message = "릴리즈 ID는 양수여야 합니다.") Long releaseId,
        @NotBlank(message = "구매 토큰은 필수입니다.") String purchaseToken,
        @NotBlank(message = "상품명은 필수입니다.") String productName,
        @NotNull(message = "단가는 필수입니다.") @Positive(message = "단가는 양수여야 합니다.") Long unitPrice,
        @Valid @NotNull(message = "배송지 정보는 필수입니다.") ShippingAddressRequest shippingAddress
) {
    public CreateReleaseOrderCommand toCommand(Long customerId) {
        return CreateReleaseOrderCommand.builder()
                .customerId(customerId)
                .sellerId(sellerId)
                .releaseId(releaseId)
                .purchaseToken(purchaseToken)
                .productName(productName)
                .unitPrice(unitPrice)
                .shippingAddress(shippingAddress.toDomain())
                .build();
    }
}
