package kr.magicbox.order.adapter.in.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.magicbox.order.domain.enums.ProductType;

public record OrderLineRequest(
        @NotNull(message = "상품 ID는 필수입니다.") @Positive(message = "상품 ID는 양수여야 합니다.") Long productId,
        @NotNull(message = "판매자 ID는 필수입니다.") @Positive(message = "판매자 ID는 양수여야 합니다.") Long sellerId,
        @NotBlank(message = "상품명은 필수입니다.") String productName,
        @NotNull(message = "수량은 필수입니다.") @Min(value = 1, message = "수량은 1 이상이어야 합니다.") Integer quantity,
        @NotNull(message = "단가는 필수입니다.") @Min(value = 0, message = "단가는 0 이상이어야 합니다.") Long unitPrice,
        @NotNull(message = "상품 타입은 필수입니다.") ProductType productType
) {}
