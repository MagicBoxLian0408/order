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
        List<Order> orders;
        if (query.customerId() != null) {
            orders = orderRepositoryPort.findByCustomerId(query.customerId());
        } else {
            orders = orderRepositoryPort.findBySellerId(query.sellerId());
        }
        return orders.stream().map(orderResultMapper::toResult).toList();
    }
}
