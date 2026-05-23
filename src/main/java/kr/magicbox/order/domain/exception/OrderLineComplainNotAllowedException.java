package kr.magicbox.order.domain.exception;

import kr.magicbox.order.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class OrderLineComplainNotAllowedException extends BusinessException {
    public OrderLineComplainNotAllowedException() {
        super("배달 중(SHIPPING) 상태의 주문 라인에서만 미수령 신고가 가능합니다.", HttpStatus.CONFLICT);
    }
}
