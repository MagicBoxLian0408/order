package kr.magicbox.order.application.port.in;

public interface HandleDeliveryCompletedUseCase {
    void handleDeliveryCompleted(Long orderId, Long orderLineId);
}
