package kr.magicbox.order.domain.vo;

import kr.magicbox.order.domain.exception.InvalidFieldException;

public record OrderLineId(Long value) {

    public OrderLineId {
        if (value == null || value <= 0) {
            throw new InvalidFieldException("주문 라인 ID는 양수여야 합니다.");
        }
    }

    public static OrderLineId of(Long value) {
        return new OrderLineId(value);
    }
}
