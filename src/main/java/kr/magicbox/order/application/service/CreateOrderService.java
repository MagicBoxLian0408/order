package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.command.CreateOrderCommand;
import kr.magicbox.order.application.dto.result.CreateOrderResult;
import kr.magicbox.order.application.dto.result.OrderLineResult;
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
    public CreateOrderResult createOrder(CreateOrderCommand command) {
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

        Order savedOrder = orderRepositoryPort.save(order);
        Long savedOrderId = savedOrder.getId().value();
        orderOutboxPort.save(OrderPrepareEvent.from(savedOrder));

        List<OrderLineResult> orderLineResults = savedOrder.getOrderLines().stream()
                .map(line -> OrderLineResult.builder()
                        .orderLineId(line.getId().value())
                        .productId(line.getProductId())
                        .productName(line.getProductName())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .build())
                .toList();

        return CreateOrderResult.builder()
                .orderId(savedOrderId)
                .sellerId(command.sellerId())
                .totalAmount(command.totalAmount())
                .orderLines(orderLineResults)
                .build();
    }
}
