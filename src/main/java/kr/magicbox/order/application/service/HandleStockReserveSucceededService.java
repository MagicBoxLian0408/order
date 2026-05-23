package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.HandleStockReserveSucceededUseCase;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandleStockReserveSucceededService implements HandleStockReserveSucceededUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Transactional
    @Override
    public void handleStockReserveSucceeded(Long orderId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));
        order.reserveStock();
        orderRepositoryPort.update(order);
    }
}
