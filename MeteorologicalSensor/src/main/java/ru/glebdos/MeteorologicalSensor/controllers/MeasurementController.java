package ru.glebdos.MeteorologicalSensor.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementDTO;
import ru.glebdos.MeteorologicalSensor.dto.MeasurementResponse;
import ru.glebdos.MeteorologicalSensor.services.MeasurementService;
import ru.glebdos.MeteorologicalSensor.util.MeasurementValidator;

import java.util.List;

import static ru.glebdos.MeteorologicalSensor.util.ErrorMessage.returnErrorMessage;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;

    }

    @GetMapping
    public MeasurementResponse getMeasurements() {
        return measurementService.findAll();
    }

    @GetMapping("/rainyDaysCount")
    public Integer getRainyDaysCount() {
        return measurementService.rainyDaysCount();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {

        measurementService.addMeasurement(measurementDTO, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);

    }




}
