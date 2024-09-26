package ru.glebdos.MeteorologicalSensor.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.models.Sensor;
import ru.glebdos.MeteorologicalSensor.repositories.SensorRepository;
import ru.glebdos.MeteorologicalSensor.util.MeasurementException;
import ru.glebdos.MeteorologicalSensor.util.SensorValidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    @InjectMocks
    private SensorService sensorService;
    @Mock
    private SensorRepository sensorRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private SensorValidator sensorValidator;


    @Test
    void findSensorByNameTest_ReturnSensor() {
        String sensorName = "Test Sensor1";
        Sensor mockSensor = new Sensor(sensorName);
       when(sensorRepository.findByName(sensorName)).thenReturn(Optional.of(mockSensor));

        Optional<Sensor> sensor = sensorService.findSensorByName(sensorName);
        assertEquals(mockSensor, sensor.get());
    }

    @Test
    void testFindSensorByName_ReturnsEmpty() {

        String sensorName = "Test Sensor1";
        when(sensorRepository.findByName(sensorName)).thenReturn(Optional.empty());


        Optional<Sensor> result = sensorService.findSensorByName(sensorName);


        Assertions.assertFalse(result.isPresent());
        verify(sensorRepository).findByName(sensorName);
    }

    @Test
    void testFindAll_ReturnsListOfSensorDTOs() {

        Sensor sensor1 = new Sensor("Test Sensor1");
        Sensor sensor2 = new Sensor("Test Sensor2");
        List<Sensor> sensors = Arrays.asList(sensor1, sensor2);

        when(sensorRepository.findAll()).thenReturn(sensors);

        SensorDTO sensorDTO1 = new SensorDTO();
        sensorDTO1.setName(sensor1.getName());
        SensorDTO sensorDTO2 = new SensorDTO();
        sensorDTO2.setName(sensor2.getName());

        when(modelMapper.map(sensor1, SensorDTO.class)).thenReturn(sensorDTO1);
        when(modelMapper.map(sensor2, SensorDTO.class)).thenReturn(sensorDTO2);


        List<SensorDTO> result = sensorService.findAll();

        assertEquals(2, result.size());
        assertEquals(sensor1.getName(), result.get(0).getName());
        assertEquals(sensor2.getName(), result.get(1).getName());
    }

    @Test
    void testFindAll_ReturnsEmptyList() {

        when(sensorRepository.findAll()).thenReturn(Collections.emptyList());
        List<SensorDTO> result = sensorService.findAll();
        Assertions.assertTrue(result.isEmpty());
        verify(sensorRepository).findAll();

    }

    @Test
    void testSave_success() {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("Temperature Sensor");
        Sensor sensor = new Sensor();
        sensor.setName(sensorDTO.getName());


        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(sensorDTO, Sensor.class)).thenReturn(sensor);

        when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);

        sensorService.save(sensorDTO, bindingResult);

        verify(sensorValidator).validate(sensorDTO, bindingResult);
        verify(bindingResult).hasErrors();
        verify(sensorRepository).save(sensor);

        assertNotNull(sensor.getCreatedTime());
        assertEquals("Temperature Sensor", sensor.getName());


    }

    @Test
    void testSave_nameIsEmpty() {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("");


        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List
                .of(new FieldError("sensorDTO", "name", "Название не должно быть пустым")));


        assertThrows(MeasurementException.class, () -> sensorService.save(sensorDTO, bindingResult));

        verify(sensorRepository,never()).save(any());

    }

    @Test
    void testSave_nameTooShort(){
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("TT");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List
                .of(new FieldError("sensorDTO","name","Название должно быть между 3 и 30 символами")));

            assertThrows(MeasurementException.class, ()-> sensorService.save(sensorDTO,bindingResult));
        verify(sensorRepository,    never()).save(any());
    }
    @Test
    void testSave_nameTooLong(){
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("Test Sensor Name Is To Loooooooong");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List
                .of(new FieldError("sensorDTO","name","Название должно быть между 3 и 30 символами")));

        assertThrows(MeasurementException.class, ()-> sensorService.save(sensorDTO,bindingResult));
        verify(sensorRepository,    never()).save(any());
    }


}
