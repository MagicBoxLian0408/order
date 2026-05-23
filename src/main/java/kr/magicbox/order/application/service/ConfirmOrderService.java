package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.command.ConfirmOrderCommand;
import kr.magicbox.order.application.port.in.ConfirmOrderUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.event.OrderConfirmedEvent;
import kr.magicbox.order.domain.exception.OrderUnauthorizedException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmOrderService implements ConfirmOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void confirmOrder(ConfirmOrderCommand command) {
        Order order = orderRepositoryPort.findById(OrderId.of(command.orderId()));

        if (!order.getSellerId().equals(command.sellerId())) {
            throw new OrderUnauthorizedException();
        }

        order.confirm();
        orderRepositoryPort.update(order);
        orderOutboxPort.save(OrderConfirmedEvent.from(order));
    }
}
