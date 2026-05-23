package kr.magicbox.order.application.port.out;

public interface PurchaseTokenValidationPort {
    /** purchase_token 검증 및 소비 (1회용). 유효하지 않으면 false */
    boolean validate(Long releaseId, Long userId, String purchaseToken);
}
