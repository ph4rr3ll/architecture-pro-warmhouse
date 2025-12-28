package com.smarthome.temperature.controller;

import com.smarthome.temperature.dto.TemperatureResponse;
import com.smarthome.temperature.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemperatureController {

    private final TemperatureService temperatureService;

    @Autowired
    public TemperatureController(TemperatureService temperatureService) {
        this.temperatureService = temperatureService;
    }

    @GetMapping("/temperature/{sensorId}")
    public TemperatureResponse getTemperature(@RequestParam(required = false) String location, @PathVariable(required = false) String sensorId) {
        return temperatureService.getTemperature(location, sensorId);
    }
}

