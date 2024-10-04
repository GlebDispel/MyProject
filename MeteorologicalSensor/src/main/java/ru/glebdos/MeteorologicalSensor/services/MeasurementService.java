package ru.glebdos.MeteorologicalSensor.services;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementDTO;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementResponse;
import ru.glebdos.MeteorologicalSensor.models.Measurement;
import ru.glebdos.MeteorologicalSensor.repositories.MeasurementRepository;
import ru.glebdos.MeteorologicalSensor.util.MeasurementValidator;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.glebdos.MeteorologicalSensor.util.ErrorMessage.returnErrorMessage;


@Service
@Transactional(readOnly = true)
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;


    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository,
                              SensorService sensorService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementRepository = measurementRepository;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    public MeasurementResponse findAll() {
        return new MeasurementResponse(measurementRepository.findAll().stream()
                .map(this::convertoMeasurementDTO)
                .collect(Collectors.toList()));
    }

    public Integer rainyDaysCount() {
        return measurementRepository.countByRainingTrue();
    }

    @Transactional
    public void addMeasurement(MeasurementDTO measurementDTO, BindingResult bindingResult) {

        measurementValidator.validate(measurementDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            returnErrorMessage(bindingResult);
        }


        Measurement localMeasurement = convertToMeasurement(measurementDTO);


        enrichMeasurement(localMeasurement);
        measurementRepository.save(localMeasurement);
    }

    private void enrichMeasurement(Measurement measurement) {
        measurement.setSensor(sensorService.findSensorByName(measurement.getSensor().getName()).get());
        measurement.setMeasurementTime(LocalDateTime.now());
    }

    private MeasurementDTO convertoMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }




}
