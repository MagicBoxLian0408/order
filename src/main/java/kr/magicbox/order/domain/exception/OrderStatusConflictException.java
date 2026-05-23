package kr.magicbox.order.domain.exception;

import kr.magicbox.order.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class OrderStatusConflictException extends BusinessException {
    public OrderStatusConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
