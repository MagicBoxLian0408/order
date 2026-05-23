package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.command.ConfirmOrderCommand;

public interface ConfirmOrderUseCase {
    void confirmOrder(ConfirmOrderCommand command);
}
