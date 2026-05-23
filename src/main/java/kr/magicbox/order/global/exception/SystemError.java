package kr.magicbox.order.global.exception;

import org.springframework.http.HttpStatus;

public class SystemError extends BaseException {

    public SystemError(String message, HttpStatus status) {
        super(message, status);
    }

    public SystemError(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }
}
