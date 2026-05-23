package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.command.CancelOrderCommand;

public interface CancelOrderUseCase {
    void cancelOrder(CancelOrderCommand command);
}
