package kr.magicbox.order.adapter.in.scheduler;

import kr.magicbox.order.application.port.in.AutoConfirmOrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoConfirmOrderScheduler {

    private final AutoConfirmOrderUseCase autoConfirmOrderUseCase;

    @Scheduled(cron = "0 0 2 * * *")
    @SchedulerLock(name = "autoConfirmDeliveredOrders", lockAtMostFor = "PT1H", lockAtLeastFor = "PT10M")
    public void autoConfirmDeliveredOrders() {
        log.info("[Scheduler] 자동 구매 확정 스케줄러 시작");
        autoConfirmOrderUseCase.autoConfirmDeliveredOrders();
        log.info("[Scheduler] 자동 구매 확정 스케줄러 완료");
    }
}
