package kr.magicbox.order.application.port.out;

import java.util.concurrent.CompletableFuture;

public interface SellerIdQueryPort {
    CompletableFuture<Long> getSellerId(Long userId);
}
