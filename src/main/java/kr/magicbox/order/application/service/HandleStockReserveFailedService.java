package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.HandleStockReserveFailedUseCase;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandleStockReserveFailedService implements HandleStockReserveFailedUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Transactional
    @Override
    public void handleStockReserveFailed(Long orderId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));
        order.failStock();
        orderRepositoryPort.update(order);
    }
}
