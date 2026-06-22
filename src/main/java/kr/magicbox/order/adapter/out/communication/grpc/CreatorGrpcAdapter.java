package kr.magicbox.order.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import kr.magicbox.order.adapter.out.communication.grpc.exception.CreatorNotFoundException;
import kr.magicbox.order.adapter.out.communication.grpc.exception.CreatorServiceUnavailableException;
import kr.magicbox.order.application.port.out.SellerIdQueryPort;
import kr.magicbox.order.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.order.grpc.creator.GetCreatorIdByUserIdRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatorGrpcAdapter implements SellerIdQueryPort {

    private final ManagedChannel creatorManagedChannel;

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "getSellerIdFallback")
    @TimeLimiter(name = "creatorService", fallbackMethod = "getSellerIdFallback")
    public CompletableFuture<Long> getSellerId(Long userId) {
        return GrpcFutures.toCompletable(
                CreatorServiceGrpc.newFutureStub(creatorManagedChannel).getCreatorIdByUserId(
                        GetCreatorIdByUserIdRequest.newBuilder().setUserId(userId).build()
                )
        ).thenApply(response -> response.getCreatorId());
    }

    @SuppressWarnings("unused")
    private CompletableFuture<Long> getSellerIdFallback(Long userId, Throwable throwable) {
        if (throwable instanceof StatusRuntimeException statusException
                && statusException.getStatus().getCode() == Status.Code.NOT_FOUND) {
            throw new CreatorNotFoundException();
        }
        log.warn("크리에이터 서비스 연결 실패");
        throw new CreatorServiceUnavailableException(throwable);
    }
}
