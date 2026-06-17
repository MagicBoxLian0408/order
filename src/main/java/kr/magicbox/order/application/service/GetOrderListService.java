package kr.magicbox.order.application.service;

import kr.magicbox.order.application.dto.query.GetOrderListQuery;
import kr.magicbox.order.application.dto.result.OrderResult;
import kr.magicbox.order.application.port.in.GetOrderListUseCase;
import kr.magicbox.order.application.service.strategy.OrderQueryStrategy;
import kr.magicbox.order.domain.exception.InvalidFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrderListService implements GetOrderListUseCase {

    private final List<OrderQueryStrategy> strategies;

    @Transactional(readOnly = true)
    @Override
    public List<OrderResult> getOrderList(GetOrderListQuery query) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(query))
                .findFirst()
                .orElseThrow(() -> new InvalidFieldException("조회 조건(customer_id 또는 seller_id)이 필요합니다."))
                .execute(query);
    }
}
