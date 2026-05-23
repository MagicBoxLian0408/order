package kr.magicbox.order.application.port.in;

public interface HandlePaymentCancelFailedUseCase {
    void handlePaymentCancelFailed(Long orderId);
}
