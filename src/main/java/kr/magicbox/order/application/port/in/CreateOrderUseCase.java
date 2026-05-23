package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.command.CreateOrderCommand;

public interface CreateOrderUseCase {
    void createOrder(CreateOrderCommand command);
}
