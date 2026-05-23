package kr.magicbox.order.domain.exception;

import kr.magicbox.order.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class OrderUnauthorizedException extends BusinessException {
    public OrderUnauthorizedException() {
        super("해당 주문에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}
