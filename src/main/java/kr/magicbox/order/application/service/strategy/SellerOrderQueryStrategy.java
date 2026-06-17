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
public class SellerOrderQueryStrategy implements OrderQueryStrategy {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderResultMapper orderResultMapper;

    @Override
    public boolean supports(GetOrderListQuery query) {
        return query.sellerId() != null;
    }

    @Override
    public List<OrderResult> execute(GetOrderListQuery query) {
        Long sellerId = query.sellerId();
        return orderRepositoryPort.findBySellerId(sellerId)
                .stream()
                .map(order -> orderResultMapper.toResult(order, sellerId))
                .toList();
    }
}
