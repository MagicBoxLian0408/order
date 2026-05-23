package kr.magicbox.order.adapter.in.web.dto.response;

import kr.magicbox.order.application.dto.result.ShippingAddressResult;
import lombok.Builder;

@Builder
public record ShippingAddressResponse(
        String recipient,
        String phone,
        String zipCode,
        String address1,
        String address2
) {
    public static ShippingAddressResponse from(ShippingAddressResult result) {
        return ShippingAddressResponse.builder()
                .recipient(result.recipient())
                .phone(result.phone())
                .zipCode(result.zipCode())
                .address1(result.address1())
                .address2(result.address2())
                .build();
    }
}
