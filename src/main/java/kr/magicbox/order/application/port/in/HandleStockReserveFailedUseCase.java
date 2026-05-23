package kr.magicbox.order.application.port.in;

public interface HandleStockReserveFailedUseCase {
    void handleStockReserveFailed(Long orderId);
}
