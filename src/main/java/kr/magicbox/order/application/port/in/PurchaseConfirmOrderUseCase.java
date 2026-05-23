package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.command.PurchaseConfirmOrderCommand;

public interface PurchaseConfirmOrderUseCase {
    void purchaseConfirmOrder(PurchaseConfirmOrderCommand command);
}
