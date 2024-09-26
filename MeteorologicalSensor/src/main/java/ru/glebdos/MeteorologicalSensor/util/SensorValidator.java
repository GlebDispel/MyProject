package ru.glebdos.MeteorologicalSensor.util;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.models.Sensor;
import ru.glebdos.MeteorologicalSensor.repositories.SensorRepository;


@Component
public class SensorValidator implements Validator {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorValidator(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensor = (SensorDTO) target;
        if (sensorRepository.findByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "", "Сенсор с таким названием уже существует");
        }
    }
}
