package ru.glebdos.MeteorologicalSensor.services;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.models.Sensor;
import ru.glebdos.MeteorologicalSensor.repositories.SensorRepository;

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


    @Test
    void findSensorByNameTest_ReturnSensor() {
        String sensorName = "Test Sensor1";
        Sensor mockSensor = new Sensor(sensorName);
        Mockito.when(sensorRepository.findByName(sensorName)).thenReturn(Optional.of(mockSensor));

        Optional<Sensor> sensor = sensorService.findSensorByName(sensorName);
        Assertions.assertEquals(mockSensor, sensor.get());
    }

    @Test
    void testFindSensorByName_ReturnsEmpty() {

        String sensorName = "Test Sensor1";
       when(sensorRepository.findByName(sensorName)).thenReturn(Optional.empty());


        Optional<Sensor> result = sensorService.findSensorByName(sensorName);


        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testFindAll_ReturnsListOfSensorDTOs() {

        Sensor sensor1 = new Sensor("Test Sensor1");
        sensor1.setId(1);
        Sensor sensor2 = new Sensor("Test Sensor2");
        sensor2.setId(2);
        List<Sensor> sensors = Arrays.asList(sensor1, sensor2);

        when(sensorRepository.findAll()).thenReturn(sensors);

        when(modelMapper.map(sensor1, SensorDTO.class)).thenReturn(new SensorDTO() {{
            setName(sensor1.getName());
        }});
        when(modelMapper.map(sensor2, SensorDTO.class)).thenReturn(new SensorDTO() {{
            setName(sensor2.getName());
        }});


        List<SensorDTO> result = sensorService.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(sensor1.getName(), result.get(0).getName());
        Assertions.assertEquals(sensor2.getName(), result.get(1).getName());
    }

    @Test
    void testFindAll_ReturnsEmptyList() {

        when(sensorRepository.findAll()).thenReturn(Collections.emptyList());
        List<SensorDTO> result = sensorService.findAll();
        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void testSave_SaveNewSensor() {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName("Temperature Sensor");

        Sensor mockSensor = new Sensor("Temperature Sensor");


        when(modelMapper.map(sensorDTO, Sensor.class)).thenReturn(mockSensor);


        sensorService.save(sensorDTO);

        verify(modelMapper, times(1)).map(sensorDTO, Sensor.class);
        verify(sensorRepository, times(1)).save(mockSensor);
    }




}
