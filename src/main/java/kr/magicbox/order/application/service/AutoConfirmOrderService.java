package kr.magicbox.order.application.service;

import kr.magicbox.order.adapter.in.scheduler.properties.AutoConfirmProperties;
import kr.magicbox.order.application.port.in.AutoConfirmOrderUseCase;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoConfirmOrderService implements AutoConfirmOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final AutoConfirmOrderChunkService autoConfirmOrderChunkService;
    private final AutoConfirmProperties autoConfirmProperties;

    @Override
    public void autoConfirmDeliveredOrders() {
        Instant threshold = Instant.now().minus(autoConfirmProperties.getDays(), ChronoUnit.DAYS);
        int chunkSize = autoConfirmProperties.getChunkSize();

        List<Order> chunk;
        do {
            chunk = orderRepositoryPort.findDeliveredBefore(threshold, chunkSize);
            chunk.forEach(autoConfirmOrderChunkService::confirmOne);
        } while (chunk.size() == chunkSize);
    }
}
