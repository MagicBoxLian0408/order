package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.HandleDeliveryCompletedUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.event.OrderDeliveredEvent;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandleDeliveryCompletedService implements HandleDeliveryCompletedUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void handleDeliveryCompleted(Long orderId, Long orderLineId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));
        order.completeDelivery(orderLineId);
        orderRepositoryPort.update(order);

        if (order.isAllDelivered()) {
            orderOutboxPort.save(OrderDeliveredEvent.from(order));
        }
    }
}
