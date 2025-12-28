package com.smarthome.telemetry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureResponse {
    private double value;
    private String location;
    private String sensorId;
    private String unit;
}
