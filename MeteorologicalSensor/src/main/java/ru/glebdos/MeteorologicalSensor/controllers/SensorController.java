package ru.glebdos.MeteorologicalSensor.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.glebdos.MeteorologicalSensor.dto.SensorDTO;
import ru.glebdos.MeteorologicalSensor.services.SensorService;
import ru.glebdos.MeteorologicalSensor.util.*;

import java.util.List;

import static ru.glebdos.MeteorologicalSensor.util.ErrorMessage.returnErrorMessage;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService,SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
    }




    @GetMapping
    public List<SensorDTO> getSensor() {
        return sensorService.findAll();
    }


    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {

        sensorService.save(sensorDTO, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }




}
