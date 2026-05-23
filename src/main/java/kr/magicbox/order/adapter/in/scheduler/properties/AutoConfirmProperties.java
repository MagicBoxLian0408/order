package kr.magicbox.order.adapter.in.scheduler.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "order.auto-confirm")
public class AutoConfirmProperties {
    private final int days;
    private final int chunkSize;
}
