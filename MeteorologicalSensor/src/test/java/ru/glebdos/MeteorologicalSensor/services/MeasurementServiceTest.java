package ru.glebdos.MeteorologicalSensor.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementDTO;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementResponse;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.models.Measurement;
import ru.glebdos.MeteorologicalSensor.models.Sensor;
import ru.glebdos.MeteorologicalSensor.repositories.MeasurementRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeasurementServiceTest {

    @InjectMocks
    private MeasurementService measurementService;
    @Mock
    private MeasurementRepository measurementRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private MeasurementResponse measurementResponse;

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
}
