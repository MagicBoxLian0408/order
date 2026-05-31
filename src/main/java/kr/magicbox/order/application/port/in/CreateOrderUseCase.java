package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.command.CreateOrderCommand;
import kr.magicbox.order.application.dto.result.CreateOrderResult;

public interface CreateOrderUseCase {
    CreateOrderResult createOrder(CreateOrderCommand command);
}
