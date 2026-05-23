package kr.magicbox.order.application.port.in;

public interface ConfirmOrderLineUseCase {
    void confirmOrderLine(Long orderId, Long orderLineId, Long sellerId);
}
