package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.command.PurchaseConfirmOrderCommand;
import kr.magicbox.order.application.port.in.PurchaseConfirmOrderUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.event.OrderPurchaseConfirmedEvent;
import kr.magicbox.order.domain.exception.OrderUnauthorizedException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseConfirmOrderService implements PurchaseConfirmOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void purchaseConfirmOrder(PurchaseConfirmOrderCommand command) {
        Order order = orderRepositoryPort.findById(OrderId.of(command.orderId()));

        if (!order.getCustomerId().equals(command.customerId())) {
            throw new OrderUnauthorizedException();
        }

        order.confirmPurchase();
        orderRepositoryPort.update(order);
        OrderPurchaseConfirmedEvent.fromShippedLines(order).forEach(orderOutboxPort::save);
    }
}
