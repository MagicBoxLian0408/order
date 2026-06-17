package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.command.CreateReleaseOrderCommand;
import kr.magicbox.order.application.dto.result.CreateOrderResult;
import kr.magicbox.order.application.dto.result.OrderLineResult;
import kr.magicbox.order.application.port.in.CreateReleaseOrderUseCase;
import kr.magicbox.order.application.port.out.OrderOutboxPort;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.application.port.out.PurchaseTokenValidationPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.aggregate.OrderLine;
import kr.magicbox.order.domain.enums.ProductType;
import kr.magicbox.order.domain.event.OrderPrepareEvent;
import kr.magicbox.order.domain.event.ReleaseSoldQuantityIncreaseEvent;
import kr.magicbox.order.domain.exception.InvalidPurchaseTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateReleaseOrderService implements CreateReleaseOrderUseCase {

    private final PurchaseTokenValidationPort purchaseTokenValidationPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderOutboxPort orderOutboxPort;

    @Override
    public CreateOrderResult createReleaseOrder(CreateReleaseOrderCommand command) {
        boolean valid = purchaseTokenValidationPort.validate(
                command.releaseId(), command.customerId(), command.purchaseToken());
        if (!valid) {
            throw new InvalidPurchaseTokenException();
        }

        return saveOrderWithOutbox(command);
    }

    @Transactional
    protected CreateOrderResult saveOrderWithOutbox(CreateReleaseOrderCommand command) {
        OrderLine orderLine = OrderLine.createBuilder()
                .productId(command.releaseId())
                .sellerId(command.sellerId())
                .productName(command.productName())
                .quantity(1)
                .unitPrice(command.unitPrice())
                .productType(ProductType.RELEASE)
                .thumbnailUrl(command.thumbnailUrl())
                .build();

        Order order = Order.createBuilder()
                .customerId(command.customerId())
                .sellerId(command.sellerId())
                .totalAmount(command.unitPrice())
                .shippingAddress(command.shippingAddress())
                .orderLines(List.of(orderLine))
                .build();

        Order savedOrder = orderRepositoryPort.save(order);
        Long savedOrderId = savedOrder.getId().value();
        orderOutboxPort.save(OrderPrepareEvent.from(savedOrder));
        orderOutboxPort.save(ReleaseSoldQuantityIncreaseEvent.of(command.releaseId()));

        OrderLineResult orderLineResult = savedOrder.getOrderLines().stream()
                .findFirst()
                .map(line -> OrderLineResult.builder()
                        .orderLineId(line.getId().value())
                        .productId(line.getProductId())
                        .productName(line.getProductName())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .productType(line.getProductType())
                        .thumbnailUrl(line.getThumbnailUrl())
                        .build())
                .orElseThrow();

        return CreateOrderResult.builder()
                .orderId(savedOrderId)
                .sellerId(command.sellerId())
                .totalAmount(command.unitPrice())
                .orderLines(List.of(orderLineResult))
                .build();
    }
}
