package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.event.OrderAutoConfirmedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoConfirmOrderChunkService {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    public void confirmOne(Order order) {
        order.confirmPurchase();
        orderRepositoryPort.update(order);
        OrderAutoConfirmedEvent.fromShippedLines(order).forEach(orderOutboxPort::save);
        log.info("[AutoConfirm] 자동 구매 확정 처리. orderId={}", order.getId().value());
    }
}
