package com.smarthome.telemetry.scheduler;

import com.smarthome.telemetry.service.TelemetryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TelemetryScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TelemetryScheduler.class);

    private final TelemetryService telemetryService;

    @Scheduled(cron = "${telemetry.collection.cron:0 */5 * * * *}")
    public void collectTelemetryData() {
        logger.info("Scheduled telemetry collection started");
        telemetryService.collectTelemetryData();
        logger.info("Scheduled telemetry collection completed");
    }
}

