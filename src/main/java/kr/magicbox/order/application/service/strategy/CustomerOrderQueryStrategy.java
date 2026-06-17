package kr.magicbox.order.application.service.strategy;

import kr.magicbox.order.application.dto.query.GetOrderListQuery;
import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.application.port.out.OrderRepositoryPort;
import kr.magicbox.order.application.service.OrderResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerOrderQueryStrategy implements OrderQueryStrategy {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderResultMapper orderResultMapper;

    @Override
    public boolean supports(GetOrderListQuery query) {
        return query.customerId() != null;
    }

    @Override
    public List<OrderResult> execute(GetOrderListQuery query) {
        return orderRepositoryPort.findByCustomerId(query.customerId())
                .stream()
                .map(orderResultMapper::toResult)
                .toList();
    }
}
