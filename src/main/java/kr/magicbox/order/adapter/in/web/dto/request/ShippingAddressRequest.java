package kr.magicbox.order.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import kr.magicbox.order.domain.vo.ShippingAddress;

public record ShippingAddressRequest(
        @NotBlank(message = "수령인은 필수입니다.") String recipient,
        @NotBlank(message = "전화번호는 필수입니다.") String phone,
        @NotBlank(message = "우편번호는 필수입니다.") String zipCode,
        @NotBlank(message = "도로명 주소는 필수입니다.") String address1,
        String address2
) {
    public ShippingAddress toDomain() {
        return ShippingAddress.of(recipient, phone, zipCode, address1, address2);
    }
}
