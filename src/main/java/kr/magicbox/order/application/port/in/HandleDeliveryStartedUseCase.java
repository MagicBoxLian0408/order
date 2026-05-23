package kr.magicbox.order.application.port.in;

public interface HandleDeliveryStartedUseCase {
    void handleDeliveryStarted(Long orderId, Long orderLineId);
}
