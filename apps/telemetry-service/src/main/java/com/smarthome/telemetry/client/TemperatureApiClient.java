package com.smarthome.telemetry.client;

import com.smarthome.telemetry.dto.TemperatureResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "temperature-api", url = "${temperature.api.base-url}")
public interface TemperatureApiClient {

    @GetMapping("/temperature/{sensorId}")
    TemperatureResponse getTemperature(
            @PathVariable("sensorId") String sensorId,
            @RequestParam(required = false) String location
    );
}

