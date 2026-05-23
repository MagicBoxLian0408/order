package kr.magicbox.order.domain.exception;

import kr.magicbox.order.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class InvalidPurchaseTokenException extends BusinessException {
    public InvalidPurchaseTokenException() {
        super("유효하지 않은 구매 토큰입니다.", HttpStatus.UNAUTHORIZED);
    }
}
