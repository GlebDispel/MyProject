package ru.glebdos.MeteorologicalSensor.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurement")
public class Measurement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Min(-100)
    @Max(100)
    @NotNull
    @Column(name = "value")
    private Double value;

    @NotNull
    @Column(name = "raining")
    private Boolean raining;

    @NotNull
    @Column(name = "measurement_time")
    private LocalDateTime measurementTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sensor", referencedColumnName = "name")
    private Sensor sensor;

    public Measurement() {}

    public Measurement(Double value, Boolean raining) {
        this.value = value;
        this.raining = raining;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public @NotNull LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(@NotNull LocalDateTime measurementTime) {
        this.measurementTime = measurementTime;
    }

    public @NotNull Sensor getSensor() {
        return sensor;
    }

    public void setSensor(@NotNull Sensor sensor) {
        this.sensor = sensor;
    }
}
