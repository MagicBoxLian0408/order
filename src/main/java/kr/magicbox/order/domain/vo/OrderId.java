package kr.magicbox.order.domain.vo;

import kr.magicbox.order.domain.exception.InvalidFieldException;

public record OrderId(Long value) {

    public OrderId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("주문 ID는 양수여야 합니다.");
        }
    }

    public static OrderId of(Long value) {
        return new OrderId(value);
    }
}
