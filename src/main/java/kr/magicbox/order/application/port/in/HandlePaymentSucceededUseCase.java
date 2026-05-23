package kr.magicbox.order.application.port.in;

public interface HandlePaymentSucceededUseCase {
    void handlePaymentSucceeded(Long orderId);
}
