package kr.magicbox.order.adapter.out.communication.grpc;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import kr.magicbox.order.adapter.out.communication.grpc.exception.CreatorNotFoundException;
import kr.magicbox.order.adapter.out.communication.grpc.exception.CreatorServiceUnavailableException;
import kr.magicbox.order.application.port.out.SellerIdQueryPort;
import kr.magicbox.order.grpc.creator.CreatorServiceGrpc;
import kr.magicbox.order.grpc.creator.GetCreatorIdByUserIdRequest;
import kr.magicbox.order.grpc.creator.GetCreatorIdByUserIdResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreatorGrpcAdapter implements SellerIdQueryPort {

    private final ManagedChannel creatorManagedChannel;

    @Override
    @CircuitBreaker(name = "creatorService", fallbackMethod = "getSellerIdFallback")
    public Long getSellerId(Long userId) {
        GetCreatorIdByUserIdRequest request = GetCreatorIdByUserIdRequest.newBuilder()
                .setUserId(userId)
                .build();

        CreatorServiceGrpc.CreatorServiceBlockingStub stub = CreatorServiceGrpc.newBlockingStub(creatorManagedChannel)
                .withDeadlineAfter(2, TimeUnit.SECONDS);
        GetCreatorIdByUserIdResponse response = stub.getCreatorIdByUserId(request);

        return response.getCreatorId();
    }

    @SuppressWarnings("unused")
    private Long getSellerIdFallback(Long userId, Throwable throwable) {
        if (throwable instanceof StatusRuntimeException statusException
                && statusException.getStatus().getCode() == Status.Code.NOT_FOUND) {
            throw new CreatorNotFoundException();
        }
        log.warn("크리에이터 서비스 연결 실패");
        throw new CreatorServiceUnavailableException(throwable);
    }
}
