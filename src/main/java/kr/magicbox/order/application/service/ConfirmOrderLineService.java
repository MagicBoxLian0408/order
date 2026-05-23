package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.ConfirmOrderLineUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.enums.OrderStatus;
import kr.magicbox.order.domain.event.OrderConfirmedEvent;
import kr.magicbox.order.domain.exception.OrderUnauthorizedException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmOrderLineService implements ConfirmOrderLineUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void confirmOrderLine(Long orderId, Long orderLineId, Long sellerId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));

        if (!order.getSellerId().equals(sellerId)) {
            throw new OrderUnauthorizedException();
        }

        order.confirmOrderLine(orderLineId);
        orderRepositoryPort.update(order);

        // 모든 라인 CONFIRMED → Order CONFIRMED 전환 시 이벤트 발행
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            orderOutboxPort.save(OrderConfirmedEvent.from(order));
        }
    }
}
