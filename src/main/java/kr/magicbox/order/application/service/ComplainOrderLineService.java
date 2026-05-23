package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.ComplainOrderLineUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.event.OrderDeliveredEvent;
import kr.magicbox.order.domain.exception.OrderUnauthorizedException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComplainOrderLineService implements ComplainOrderLineUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void complainOrderLine(Long orderId, Long orderLineId, Long customerId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));

        if (!order.getCustomerId().equals(customerId)) {
            throw new OrderUnauthorizedException();
        }

        order.complainOrderLine(orderLineId);
        orderRepositoryPort.update(order);

        if (order.isAllDelivered()) {
            orderOutboxPort.save(OrderDeliveredEvent.from(order));
        }
    }
}
