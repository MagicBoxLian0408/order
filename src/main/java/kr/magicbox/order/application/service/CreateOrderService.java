package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.command.CreateOrderCommand;
import kr.magicbox.order.application.port.in.CreateOrderUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.aggregate.OrderLine;
import kr.magicbox.order.domain.event.OrderPrepareEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Transactional
    @Override
    public void createOrder(CreateOrderCommand command) {
        List<OrderLine> orderLines = command.orderLines().stream()
                .map(line -> OrderLine.createBuilder()
                        .productId(line.productId())
                        .sellerId(line.sellerId())
                        .productName(line.productName())
                        .quantity(line.quantity())
                        .unitPrice(line.unitPrice())
                        .build())
                .toList();

        Order order = Order.createBuilder()
                .customerId(command.customerId())
                .sellerId(command.sellerId())
                .totalAmount(command.totalAmount())
                .shippingAddress(command.shippingAddress())
                .orderLines(orderLines)
                .build();

        Long savedOrderId = orderRepositoryPort.save(order);
        orderOutboxPort.save(OrderPrepareEvent.from(savedOrderId, order));
    }
}
