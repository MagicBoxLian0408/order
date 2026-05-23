package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.command.CancelOrderCommand;
import kr.magicbox.order.application.port.in.CancelOrderUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.enums.OrderStatus;
import kr.magicbox.order.domain.event.OrderCancelEvent;
import kr.magicbox.order.domain.exception.OrderUnauthorizedException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void cancelOrder(CancelOrderCommand command) {
        Order order = orderRepositoryPort.findById(OrderId.of(command.orderId()));

        if (!order.getCustomerId().equals(command.customerId())) {
            throw new OrderUnauthorizedException();
        }

        order.cancelOrderLine(command.orderLineId());
        orderRepositoryPort.update(order);

        // 모든 라인이 취소 요청 완료(Order CANCELLING)된 시점에 order-level 이벤트 1회 발행
        if (order.getStatus() == OrderStatus.CANCELLING) {
            orderOutboxPort.save(OrderCancelEvent.from(order, command.reason()));
        }
    }
}
