package kr.magicbox.order.adapter.in.scheduler;

import kr.magicbox.order.application.port.in.AutoConfirmOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoConfirmOrderScheduler {

    private static final String LOCK_KEY = "scheduler:auto-confirm-order";

    private final AutoConfirmOrderUseCase autoConfirmOrderUseCase;
    private final RedissonClient redissonClient;

    @Scheduled(cron = "0 0 2 * * *")
    public void autoConfirmDeliveredOrders() {
        RLock lock = redissonClient.getLock(LOCK_KEY);
        if (!lock.tryLock()) {
            return;
        }
        try {
            log.info("[Scheduler] 자동 구매 확정 스케줄러 시작");
            autoConfirmOrderUseCase.autoConfirmDeliveredOrders();
            log.info("[Scheduler] 자동 구매 확정 스케줄러 완료");
        } finally {
            lock.unlock();
        }
    }
}
