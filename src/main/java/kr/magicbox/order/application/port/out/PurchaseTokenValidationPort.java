package kr.magicbox.order.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface PurchaseTokenValidationPort {
    /** purchase_token 검증 및 소비 (1회용). 유효하지 않으면 false */
    CompletableFuture<Boolean> validate(Long releaseId, Long userId, String purchaseToken);
}
