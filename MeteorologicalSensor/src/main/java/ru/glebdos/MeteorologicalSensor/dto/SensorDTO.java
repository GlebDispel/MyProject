package ru.glebdos.MeteorologicalSensor.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SensorDTO {


    @NotEmpty
    @Size(min = 3, max = 30, message = "Название должно быть между 3 и 30 символами")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
