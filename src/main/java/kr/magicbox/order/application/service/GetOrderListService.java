package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.query.GetOrderListQuery;
import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.application.port.in.GetOrderListUseCase;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.domain.aggregate.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrderListService implements GetOrderListUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderResultMapper orderResultMapper;

    @Transactional(readOnly = true)
    @Override
    public List<OrderResult> getOrderList(GetOrderListQuery query) {
        if (query.customerId() != null) {
            List<Order> orders = orderRepositoryPort.findByCustomerId(query.customerId());
            return orders.stream().map(orderResultMapper::toResult).toList();
        } else {
            Long sellerId = query.sellerId();
            List<Order> orders = orderRepositoryPort.findBySellerId(sellerId);
            return orders.stream()
                    .map(order -> orderResultMapper.toResult(order, sellerId))
                    .toList();
        }
    }
}
