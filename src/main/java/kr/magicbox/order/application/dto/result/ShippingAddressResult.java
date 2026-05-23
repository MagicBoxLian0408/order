package kr.magicbox.order.application.dto.result;

import kr.magicbox.order.domain.vo.ShippingAddress;
import lombok.Builder;

@Builder
public record ShippingAddressResult(
        String recipient,
        String phone,
        String zipCode,
        String address1,
        String address2
) {
    public static ShippingAddressResult from(ShippingAddress domain) {
        return ShippingAddressResult.builder()
                .recipient(domain.recipient())
                .phone(domain.phone())
                .zipCode(domain.zipCode())
                .address1(domain.address1())
                .address2(domain.address2())
                .build();
    }
}
