package com.smarthome.telemetry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    private Integer id;
    private String name;
    private String type;
    private String location;
    private Double value;
    private String unit;
    private String status;
    private Instant lastUpdated;
    private Instant createdAt;
}
