package com.smarthome.telemetry.repository;

import com.smarthome.telemetry.entity.TelemetryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TelemetryRepository extends JpaRepository<TelemetryRecord, Long> {

    Page<TelemetryRecord> findBySensorId(Integer sensorId, Pageable pageable);

    Page<TelemetryRecord> findBySensorType(String sensorType, Pageable pageable);

    @Query("SELECT t FROM TelemetryRecord t WHERE " +
           "(:sensorId IS NULL OR t.sensorId = :sensorId) AND " +
           "(:sensorType IS NULL OR t.sensorType = :sensorType) AND " +
           "(:startTime IS NULL OR t.timestamp >= :startTime) AND " +
           "(:endTime IS NULL OR t.timestamp <= :endTime)")
    Page<TelemetryRecord> findWithFilters(
            @Param("sensorId") Integer sensorId,
            @Param("sensorType") String sensorType,
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime,
            Pageable pageable
    );

    List<TelemetryRecord> findBySensorIdAndTimestampBetween(
            Integer sensorId, Instant startTime, Instant endTime
    );
}

