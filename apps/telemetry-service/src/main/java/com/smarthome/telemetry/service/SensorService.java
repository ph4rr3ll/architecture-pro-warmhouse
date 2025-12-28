package com.smarthome.telemetry.service;

import com.smarthome.telemetry.entity.Sensor;
import com.smarthome.telemetry.repository.SensorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;

    @Transactional(readOnly = true)
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Sensor> getSensorById(Integer id) {
        return sensorRepository.findById(id);
    }
}
