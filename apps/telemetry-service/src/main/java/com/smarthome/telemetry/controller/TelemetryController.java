package com.smarthome.telemetry.controller;

import com.smarthome.telemetry.dto.TelemetryResponse;
import com.smarthome.telemetry.entity.TelemetryRecord;
import com.smarthome.telemetry.service.TelemetryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/telemetry")
@AllArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @GetMapping
    public ResponseEntity<Page<TelemetryResponse>> getTelemetry(
            @RequestParam(required = false) Integer sensorId,
            @RequestParam(required = false) String sensorType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var records = telemetryService.getTelemetryWithFilters(sensorId,
            sensorType,
            startTime,
            endTime,
            pageable
        );
        var response = records.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sensor/{sensorId}")
    public ResponseEntity<Page<TelemetryResponse>> getTelemetryBySensorId(
            @PathVariable Integer sensorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        var records = telemetryService.getTelemetryBySensorId(sensorId, pageable);
        var response = records.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sensor/{sensorId}/range")
    public ResponseEntity<List<TelemetryResponse>> getTelemetryBySensorIdAndTimeRange(
            @PathVariable Integer sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {
        var records = telemetryService.getTelemetryBySensorIdAndTimeRange(
                sensorId, startTime, endTime
        );
        var response = records.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/collect")
    public ResponseEntity<String> triggerCollection() {
        try {
            telemetryService.collectTelemetryData();
            return ResponseEntity.ok("Telemetry collection triggered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error triggering telemetry collection: " + e.getMessage());
        }
    }

    private TelemetryResponse toResponse(TelemetryRecord record) {
        return new TelemetryResponse(
                record.getId(),
                record.getSensorId(),
                record.getSensorName(),
                record.getSensorType(),
                record.getValue(),
                record.getUnit(),
                record.getStatus(),
                record.getTimestamp(),
                record.getCreatedAt()
        );
    }
}

