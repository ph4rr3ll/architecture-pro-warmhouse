package com.smarthome.telemetry.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "telemetry", indexes = {
    @Index(name = "idx_telemetry_sensor_id", columnList = "sensor_id"),
    @Index(name = "idx_telemetry_timestamp", columnList = "timestamp"),
    @Index(name = "idx_telemetry_sensor_timestamp", columnList = "sensor_id,timestamp")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sensor_id", nullable = false)
    private Integer sensorId;

    @Column(name = "sensor_name", nullable = false)
    private String sensorName;

    @Column(name = "sensor_type", nullable = false)
    private String sensorType;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "unit")
    private String unit;

    @Column(name = "status")
    private String status;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    // Constructor without id and createdAt for creating new records
    public TelemetryRecord(Integer sensorId, String sensorName, String sensorType, 
                          Double value, String unit, String status, Instant timestamp) {
        this.sensorId = sensorId;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.value = value;
        this.unit = unit;
        this.status = status;
        this.timestamp = timestamp;
    }
}
