package ru.glebdos.MeteorologicalSensor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementDTO;
import ru.glebdos.MeteorologicalSensor.models.Measurement;
import ru.glebdos.MeteorologicalSensor.repositories.SensorRepository;

@Component
public class MeasurementValidator implements Validator {

    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementValidator(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeasurementDTO measurement = (MeasurementDTO) target;

        if(measurement.getSensor() == null)
            return;

        if (sensorRepository.findByName(measurement.getSensor().getName()).isEmpty())
            errors.rejectValue("sensor", "", "Сенсор с таким именем не найден");
    }
}
