package ru.glebdos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        final String sensorName = "testSensor";
        sensorRegistration(sensorName);

        Random rand = new Random();
        double maxTemp = 45.0;

        for (int i = 0; i < 10; i++) {
            double temp = (double) Math.round((rand.nextDouble() * maxTemp) * 10) /10;
            sendMeasurement(temp,rand.nextBoolean(),sensorName);
        }

        for (int i = 0; i < 10; i++) {

            getRequestWithJSONData("http://localhost:8080/measurements",i);
        }


    }

    private static void sensorRegistration(String sensorName){
        final String url = "http://localhost:8080/sensors/registration";
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name",sensorName);

        postRequestWithJSONData(jsonData,url);
    }

    private static void sendMeasurement(Double value,Boolean raining,String sensorName){
        final String url = "http://localhost:8080/measurements/add";
        Map<String,Object> jsonData = new HashMap<>();
        jsonData.put("value",value);
        jsonData.put("raining",raining);
        jsonData.put("sensor",Map.of("name",sensorName));

        postRequestWithJSONData(jsonData,url);
    }

    private static void postRequestWithJSONData(Map<String,Object> jsonData, String url){
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try{
        restTemplate.postForObject(url,request,String.class);
        System.out.println("Запрос успешно отправлен");
        }catch (HttpClientErrorException e){
            System.out.println("Ошибка");
            System.out.println(e.getMessage());
        }
    }

    private static void getRequestWithJSONData(String url, int counter){
        final RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url,String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode obj = mapper.readTree(response);
            System.out.println("Информация о сенсере : Температура - " + obj.get(counter).get("value") + ", Идет дождь - "
                    + obj.get(counter).get("raining") + ", Название - " + obj.get(counter).get("sensor").get("name"));
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка");
            System.out.println(e.getMessage());
        }
    }
}
