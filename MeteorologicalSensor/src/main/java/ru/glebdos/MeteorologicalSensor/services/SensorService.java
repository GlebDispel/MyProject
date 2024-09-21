package ru.glebdos.MeteorologicalSensor.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.models.Sensor;
import ru.glebdos.MeteorologicalSensor.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public SensorService(SensorRepository sensorRepository, ModelMapper modelMapper) {
        this.sensorRepository = sensorRepository;
        this.modelMapper = modelMapper;
    }

    public List<SensorDTO> findAll() {
        return sensorRepository.findAll().stream().map(this::convertToSensorDTO).collect(Collectors.toList());
    }


    public Optional<Sensor> findSensorByName(String name) {
        return sensorRepository.findByName(name);
    }

    @Transactional
    public void save(SensorDTO sensorDTO) {
        Sensor localSensor = convertToSensor(sensorDTO);
        enrichSensor(localSensor);
        sensorRepository.save(localSensor);
    }

    private void enrichSensor(Sensor sensor) {
        sensor.setCreatedTime(LocalDateTime.now());
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }


}
