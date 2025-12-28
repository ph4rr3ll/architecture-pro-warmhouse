package com.smarthome.temperature.service;

import com.smarthome.temperature.dto.TemperatureResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
public class TemperatureService {

    private final Random random = new Random();

    public TemperatureResponse getTemperature(String location, String sensorId) {        
        // If no location is provided, use a default based on sensor ID
        if (Objects.isNull(location) || location.isEmpty()) {
            switch (sensorId) {
                case "1":
                    location = "Living Room";
                case "2":
                    location = "Bedroom";
                case "3":
                    location = "Kitchen";
                default:
                    location = "Unknown";
            }
        }

        // If no sensor ID is provided, generate one based on location
        if (Objects.isNull(sensorId) || sensorId.isEmpty()) {
            switch (location) {
                case "Living Room":
                    sensorId = "1";
                case "Bedroom":
                    sensorId = "2";
                case "Kitchen":
                    sensorId = "3";
                default:
                    sensorId = "0";
            }
        }

        // Generate random temperature between 15.0 and 30.0 degrees Celsius
        double temperature = 15.0 + (random.nextDouble() * 15.0);
        temperature = Math.round(temperature * 10.0) / 10.0; // Round to 1 decimal place

        return new TemperatureResponse(temperature, location, sensorId, "Celsius");
    }
}

