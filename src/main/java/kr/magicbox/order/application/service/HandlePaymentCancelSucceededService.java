package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.HandlePaymentCancelSucceededUseCase;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandlePaymentCancelSucceededService implements HandlePaymentCancelSucceededUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Transactional
    @Override
    public void handlePaymentCancelSucceeded(Long orderId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));
        order.completeCancellation();
        orderRepositoryPort.update(order);
    }
}
