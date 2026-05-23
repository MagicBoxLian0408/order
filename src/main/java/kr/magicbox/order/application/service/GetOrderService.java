package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.query.GetOrderQuery;
import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.application.port.in.GetOrderUseCase;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import kr.magicbox.order.domain.exception.OrderUnauthorizedException;
import kr.magicbox.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetOrderService implements GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderResultMapper orderResultMapper;

    @Transactional(readOnly = true)
    @Override
    public OrderResult getOrder(GetOrderQuery query) {
        Order order = orderRepositoryPort.findById(OrderId.of(query.orderId()));

        if (!order.getCustomerId().equals(query.requesterId()) && !order.getSellerId().equals(query.requesterId())) {
            throw new OrderUnauthorizedException();
        }

        return orderResultMapper.toResult(order);
    }
}
