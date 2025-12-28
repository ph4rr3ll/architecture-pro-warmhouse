package com.smarthome.telemetry.service;

import com.smarthome.telemetry.client.TemperatureApiClient;
import com.smarthome.telemetry.entity.Sensor;
import com.smarthome.telemetry.dto.TemperatureResponse;
import com.smarthome.telemetry.entity.TelemetryRecord;
import com.smarthome.telemetry.repository.TelemetryRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class TelemetryService {

    private static final Logger logger = LoggerFactory.getLogger(TelemetryService.class);

    private final TelemetryRepository telemetryRepository;
    private final SensorService sensorService;
    private final TemperatureApiClient temperatureApiClient;

    @Transactional
    public void collectTelemetryData() {
        logger.info("Starting telemetry data collection");
        
        try {
            List<Sensor> sensors = sensorService.getAllSensors();
            logger.info("Found {} sensors to process", sensors.size());

            for (Sensor sensor : sensors) {
                try {
                    if ("temperature".equalsIgnoreCase(sensor.getType())) {
                        collectTemperatureTelemetry(sensor);
                    } else {
                        logger.debug("Skipping sensor {} - type {} not supported yet", sensor.getId(), sensor.getType());
                    }
                } catch (Exception e) {
                    logger.error("Error collecting telemetry for sensor {}: {}", sensor.getId(), e.getMessage(), e);
                }
            }

            logger.info("Completed telemetry data collection");
        } catch (Exception e) {
            logger.error("Error during telemetry data collection: {}", e.getMessage(), e);
        }
    }

    private void collectTemperatureTelemetry(Sensor sensor) {
        try {
            logger.debug("Collecting temperature telemetry for sensor ID: {}", sensor.getId());
            
            TemperatureResponse tempResponse;
            try {
                logger.debug("Fetching temperature data for sensor ID: {}", sensor.getId());
                tempResponse = temperatureApiClient.getTemperature(
                        String.valueOf(sensor.getId()), null
                );
                logger.debug("Successfully fetched temperature data for sensor ID: {}", sensor.getId());
            } catch (Exception e) {
                logger.error("Error fetching temperature data for sensor ID {}: {}", sensor.getId(), e.getMessage());
                throw new RuntimeException("Failed to fetch temperature data from temperature-api", e);
            }
            
            TelemetryRecord record = new TelemetryRecord(
                    sensor.getId(),
                    sensor.getName(),
                    sensor.getType(),
                    tempResponse.getValue(),
                    tempResponse.getUnit(),
                    "active",
                    Instant.now()
            );

            telemetryRepository.save(record);
            logger.debug("Successfully saved telemetry record for sensor ID: {}", sensor.getId());
        } catch (Exception e) {
            logger.error("Error collecting temperature telemetry for sensor {}: {}", sensor.getId(), e.getMessage());
            throw e;
        }
    }

    public Page<TelemetryRecord> getTelemetryWithFilters(
            Integer sensorId,
            String sensorType,
            Instant startTime,
            Instant endTime,
            Pageable pageable) {
        
        return telemetryRepository.findWithFilters(
                sensorId, sensorType, startTime, endTime, pageable
        );
    }

    public Page<TelemetryRecord> getTelemetryBySensorId(Integer sensorId, Pageable pageable) {
        return telemetryRepository.findBySensorId(sensorId, pageable);
    }

    public Page<TelemetryRecord> getTelemetryBySensorType(String sensorType, Pageable pageable) {
        return telemetryRepository.findBySensorType(sensorType, pageable);
    }

    public List<TelemetryRecord> getTelemetryBySensorIdAndTimeRange(
            Integer sensorId, Instant startTime, Instant endTime) {
        return telemetryRepository.findBySensorIdAndTimestampBetween(sensorId, startTime, endTime);
    }
}

