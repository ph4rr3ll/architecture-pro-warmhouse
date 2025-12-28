package com.smarthome.telemetry.repository;

import com.smarthome.telemetry.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    List<Sensor> findAllByOrderByIdAsc();

    Optional<Sensor> findById(Integer id);
}

