package kr.magicbox.order.adapter.in.scheduler;

import kr.magicbox.order.adapter.in.scheduler.properties.AutoConfirmProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties(AutoConfirmProperties.class)
@Configuration
public class SchedulerConfiguration {
}
