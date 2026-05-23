package kr.magicbox.order.application.service;

import kr.magicbox.order.application.port.in.HandleOrderPrepareUseCase;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandleOrderPrepareService implements HandleOrderPrepareUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Transactional
    @Override
    public void handleOrderPrepare(Long orderId) {
        Order order = orderRepositoryPort.findById(OrderId.of(orderId));
        order.prepare();
        orderRepositoryPort.update(order);
    }
}
