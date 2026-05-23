package kr.magicbox.order.domain.exception;

import kr.magicbox.order.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
