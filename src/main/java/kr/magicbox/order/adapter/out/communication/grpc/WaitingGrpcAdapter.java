package kr.magicbox.order.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import kr.magicbox.order.adapter.out.communication.grpc.exception.WaitingServiceUnavailableException;
import kr.magicbox.order.application.port.out.PurchaseTokenValidationPort;
import kr.magicbox.order.grpc.waiting.ValidatePurchaseTokenRequest;
import kr.magicbox.order.grpc.waiting.ValidatePurchaseTokenResponse;
import kr.magicbox.order.grpc.waiting.WaitingServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingGrpcAdapter implements PurchaseTokenValidationPort {

    private final ManagedChannel waitingManagedChannel;

    @Override
    @CircuitBreaker(name = "waitingService", fallbackMethod = "validateFallback")
    public boolean validate(Long releaseId, Long userId, String purchaseToken) {
        ValidatePurchaseTokenRequest request = ValidatePurchaseTokenRequest.newBuilder()
                .setReleaseId(releaseId)
                .setUserId(userId)
                .setPurchaseToken(purchaseToken)
                .build();

        WaitingServiceGrpc.WaitingServiceBlockingStub stub = WaitingServiceGrpc.newBlockingStub(waitingManagedChannel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
        ValidatePurchaseTokenResponse response = stub.validatePurchaseToken(request);

        return response.getValid();
    }

    @SuppressWarnings("unused")
    private boolean validateFallback(Long releaseId, Long userId, String purchaseToken, Throwable throwable) {
        log.warn("대기열 서비스 연결 실패");
        throw new WaitingServiceUnavailableException(throwable);
    }
}
