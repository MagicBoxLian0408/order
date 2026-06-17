package kr.magicbox.order.application.service.strategy;

import kr.magicbox.order.application.dto.query.GetOrderListQuery;
import kr.magicbox.order.application.dto.result.OrderResult;

import java.util.List;

public interface OrderQueryStrategy {
    boolean supports(GetOrderListQuery query);
    List<OrderResult> execute(GetOrderListQuery query);
}
