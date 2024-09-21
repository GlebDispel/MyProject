package ru.glebdos.MeteorologicalSensor.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ErrorMessage {

    public static void returnErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMessage.append(fieldError.getField())
                    .append(" – ")
                    .append(fieldError.getDefaultMessage() == null ? fieldError.getCode() : fieldError.getDefaultMessage())
                    .append(";");
        }
        throw new MeasurementException(errorMessage.toString());
    }
}
