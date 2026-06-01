package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.command.CreateReleaseOrderCommand;
import kr.magicbox.order.application.dto.result.CreateOrderResult;

public interface CreateReleaseOrderUseCase {
    CreateOrderResult createReleaseOrder(CreateReleaseOrderCommand command);
}
