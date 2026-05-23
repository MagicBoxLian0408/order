package kr.magicbox.order.domain.vo;

import kr.magicbox.order.domain.exception.InvalidFieldException;

public record ShippingAddress(
        String recipient,
        String phone,
        String zipCode,
        String address1,
        String address2
) {

    public ShippingAddress {
        if (recipient == null || recipient.isBlank()) {
            throw new InvalidFieldException("수령인은 필수 값입니다.");
        }
        if (phone == null || phone.isBlank()) {
            throw new InvalidFieldException("전화번호는 필수 값입니다.");
        }
        if (zipCode == null || zipCode.isBlank()) {
            throw new InvalidFieldException("우편번호는 필수 값입니다.");
        }
        if (address1 == null || address1.isBlank()) {
            throw new InvalidFieldException("도로명 주소는 필수 값입니다.");
        }
    }

    public static ShippingAddress of(String recipient, String phone, String zipCode, String address1, String address2) {
        return new ShippingAddress(recipient, phone, zipCode, address1, address2);
    }
}
