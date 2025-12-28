package com.smarthome.telemetry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryResponse {
    private Long id;
    private Integer sensorId;
    private String sensorName;
    private String sensorType;
    private Double value;
    private String unit;
    private String status;
    private Instant timestamp;
    private Instant createdAt;
}
