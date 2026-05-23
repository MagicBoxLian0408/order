package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.query.GetOrderListQuery;
import kr.magicbox.order.application.dto.result.OrderResult;

import java.util.List;

public interface GetOrderListUseCase {
    List<OrderResult> getOrderList(GetOrderListQuery query);
}
