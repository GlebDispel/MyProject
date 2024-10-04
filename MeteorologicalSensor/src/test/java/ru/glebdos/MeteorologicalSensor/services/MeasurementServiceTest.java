package ru.glebdos.MeteorologicalSensor.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementDTO;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementResponse;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.models.Measurement;
import ru.glebdos.MeteorologicalSensor.models.Sensor;
import ru.glebdos.MeteorologicalSensor.repositories.MeasurementRepository;
import ru.glebdos.MeteorologicalSensor.repositories.SensorRepository;
import ru.glebdos.MeteorologicalSensor.util.MeasurementException;
import ru.glebdos.MeteorologicalSensor.util.MeasurementValidator;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Optional.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeasurementServiceTest {

    @InjectMocks
    private MeasurementService measurementService;
    @Mock
    private MeasurementRepository measurementRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SensorRepository sensorRepository;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private MeasurementValidator measurementValidator;
    @Mock
    private SensorService sensorService;

    @Test
    void findAllMeasurementsTest_ReturnMeasurementsResponse(){
        Measurement measurement1 = new Measurement(22.0,true,new Sensor("Sensor1"));
        Measurement measurement2 = new Measurement(33.0,false,new Sensor("Sensor2"));
        List<Measurement> measurements = Arrays.asList(measurement1, measurement2);

        when(measurementRepository.findAll()).thenReturn(measurements);

        MeasurementDTO measurementDTO1 = new MeasurementDTO(22.0,true,new SensorDTO(){{setName("Sensor1");}});
        MeasurementDTO measurementDTO2 = new MeasurementDTO(33.0,false,new SensorDTO(){{setName("Sensor2");}});


        when(modelMapper.map(measurement1, MeasurementDTO.class)).thenReturn(measurementDTO1);
        when(modelMapper.map(measurement2, MeasurementDTO.class)).thenReturn(measurementDTO2);

        MeasurementResponse result = measurementService.findAll();

        assertEquals(2, result.getMeasurements().size());

        assertEquals(measurement1.getValue(), result.getMeasurements().get(0).getValue());
        assertEquals(measurement1.getRaining(), result.getMeasurements().get(0).getRaining());
        assertEquals(measurement1.getSensor().getName(),result.getMeasurements().get(0).getSensor().getName());

        assertEquals(measurement2.getValue(), result.getMeasurements().get(1).getValue());
        assertEquals(measurement2.getRaining(), result.getMeasurements().get(1).getRaining());
        assertEquals(measurement2.getSensor().getName(),result.getMeasurements().get(1).getSensor().getName());

    }

    @Test
    void findAllMeasurementsTest_ReturnEmptyList(){

        when(measurementRepository.findAll()).thenReturn(Collections.emptyList());
        MeasurementResponse result = measurementService.findAll();
        assertTrue(result.getMeasurements().isEmpty());
        verify(measurementRepository).findAll();

    }

    @Test
    void rainyDaysCountTest(){
        Integer expectedCount = 5;
        when(measurementRepository.countByRainingTrue()).thenReturn(expectedCount);
        Integer actualCount =  measurementService.rainyDaysCount();
        assertEquals(actualCount,expectedCount);
        verify(measurementRepository, Mockito.times(1)).countByRainingTrue();
    }

   @Test
    void addMeasurementTest_Success(){
        MeasurementDTO measurementDTO = new MeasurementDTO(22.0,true,new SensorDTO(){{setName("Sensor1");}});
        Measurement measurement1 = new Measurement(22.0,true,new Sensor("Sensor1"));
        when(bindingResult.hasErrors()).thenReturn(false);
        when(modelMapper.map(measurementDTO,Measurement.class)).thenReturn(measurement1);
        when(measurementRepository.save(measurement1)).thenReturn(measurement1);
        when(sensorService.findSensorByName("Sensor1")).thenReturn(Optional.of(new Sensor("Sensor1")));

        measurementService.addMeasurement(measurementDTO,bindingResult);

        verify(measurementValidator).validate(measurementDTO,bindingResult);
        verify(bindingResult).hasErrors();
        verify(measurementRepository).save(measurement1);

        assertNotNull(measurement1.getMeasurementTime());
        assertEquals("Sensor1", measurement1.getSensor().getName());


   }

   @Test
    void addMeasurementTest_Failure(){
       MeasurementDTO measurementDTO = new MeasurementDTO(22.0,true,new SensorDTO(){{setName("Sensor1");}});
       when(bindingResult.hasErrors()).thenReturn(true);
       when(bindingResult.getFieldErrors())
               .thenReturn(List.of(new FieldError("sensor","name","Сенсор с таким именем не найден")));

       assertThrows(MeasurementException.class, () -> measurementService.addMeasurement(measurementDTO,bindingResult) );
       verify(measurementRepository,never()).save(any());
   }
}
