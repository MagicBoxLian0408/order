package kr.magicbox.order.application.port.in;

public interface ComplainOrderLineUseCase {
    void complainOrderLine(Long orderId, Long orderLineId, Long customerId);
}
