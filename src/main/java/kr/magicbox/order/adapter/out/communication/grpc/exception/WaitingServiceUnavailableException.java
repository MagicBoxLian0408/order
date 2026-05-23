package kr.magicbox.order.adapter.out.communication.grpc.exception;

import kr.magicbox.order.global.exception.SystemError;
import org.springframework.http.HttpStatus;

@SuppressWarnings("java:S110")
public class WaitingServiceUnavailableException extends SystemError {

    public WaitingServiceUnavailableException(Throwable cause) {
        super("대기열 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
