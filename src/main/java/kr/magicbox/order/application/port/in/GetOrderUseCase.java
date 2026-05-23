package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.query.GetOrderQuery;
import kr.magicbox.order.application.dto.result.OrderResult;

public interface GetOrderUseCase {
    OrderResult getOrder(GetOrderQuery query);
}
