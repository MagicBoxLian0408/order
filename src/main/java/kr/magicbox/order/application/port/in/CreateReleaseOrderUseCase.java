package kr.magicbox.order.application.port.in;

import kr.magicbox.order.application.dto.command.CreateReleaseOrderCommand;

public interface CreateReleaseOrderUseCase {
    void createReleaseOrder(CreateReleaseOrderCommand command);
}
