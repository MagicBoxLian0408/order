package kr.magicbox.order.adapter.out.communication.grpc;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import kr.magicbox.order.adapter.out.communication.grpc.exception.WaitingServiceUnavailableException;
import kr.magicbox.order.application.port.out.PurchaseTokenValidationPort;
import kr.magicbox.order.grpc.waiting.ValidatePurchaseTokenRequest;
import kr.magicbox.order.grpc.waiting.WaitingServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingGrpcAdapter implements PurchaseTokenValidationPort {

    private final ManagedChannel waitingManagedChannel;

    @Override
    @CircuitBreaker(name = "waitingService", fallbackMethod = "validateFallback")
    @TimeLimiter(name = "waitingService", fallbackMethod = "validateFallback")
    public CompletableFuture<Boolean> validate(Long releaseId, Long userId, String purchaseToken) {
        return GrpcFutures.toCompletable(
                WaitingServiceGrpc.newFutureStub(waitingManagedChannel).validatePurchaseToken(
                        ValidatePurchaseTokenRequest.newBuilder()
                                .setReleaseId(releaseId)
                                .setUserId(userId)
                                .setPurchaseToken(purchaseToken)
                                .build()
                )
        ).thenApply(response -> response.getValid());
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Boolean> validateFallback(Long releaseId, Long userId, String purchaseToken, Throwable throwable) {
        log.warn("대기열 서비스 연결 실패");
        throw new WaitingServiceUnavailableException(throwable);
    }
}
