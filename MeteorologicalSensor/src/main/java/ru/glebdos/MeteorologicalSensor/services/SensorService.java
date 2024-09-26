package ru.glebdos.MeteorologicalSensor.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.validation.BindingResult;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.models.Sensor;
import ru.glebdos.MeteorologicalSensor.repositories.SensorRepository;
import ru.glebdos.MeteorologicalSensor.util.SensorValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.glebdos.MeteorologicalSensor.util.ErrorMessage.returnErrorMessage;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;


    @Autowired
    public SensorService(SensorRepository sensorRepository, ModelMapper modelMapper, SensorValidator sensorValidator) {
        this.sensorRepository = sensorRepository;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    public List<SensorDTO> findAll() {
        return sensorRepository.findAll().stream().map(this::convertToSensorDTO).collect(Collectors.toList());
    }


    public Optional<Sensor> findSensorByName(String name) {
        return sensorRepository.findByName(name);
    }

    @Transactional
    public void save(SensorDTO sensorDTO, BindingResult bindingResult) {

        sensorValidator.validate(sensorDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            returnErrorMessage(bindingResult);
        }

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
