package kr.magicbox.order.application.port.in;

public interface HandlePaymentFailedUseCase {
    void handlePaymentFailed(Long orderId);
}
