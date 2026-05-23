package kr.magicbox.order.domain.exception;

import kr.magicbox.order.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class OrderLineNotFoundException extends BusinessException {

    public OrderLineNotFoundException() {
        super("주문 라인을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
