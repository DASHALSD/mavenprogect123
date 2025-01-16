package com.example;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class App {
    public static void main(String[] args) {
        String url = "http://94.198.50.185:7081/api/users";

        RestTemplate restTemplate = new RestTemplate();

        // 1. GET запрос - получить пользователей и сохранить JSESSIONID
        ResponseEntity<String> getResponse = restTemplate.getForEntity(url, String.class);

        HttpHeaders headers = getResponse.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        String sessionId = cookies.get(0).split(";")[0]; // Извлекаем JSESSIONID
        System.out.println("Session ID: " + sessionId);

        // 2. POST запрос - добавить нового пользователя
        User newUser = new User(3L, "James", "Brown", (byte) 25);

        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.set("Cookie", sessionId);
        postHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<User> postEntity = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(url, postEntity, String.class);
        String codePart1 = postResponse.getBody();
        System.out.println("Code part 1: " + codePart1);

        // 3. PUT запрос - изменить пользователя
        newUser.setName("Thomas");
        newUser.setLastName("Shelby");

        HttpEntity<User> putEntity = new HttpEntity<>(newUser, postHeaders);
        ResponseEntity<String> putResponse = restTemplate.exchange(url, HttpMethod.PUT, putEntity, String.class);
        String codePart2 = putResponse.getBody();
        System.out.println("Code part 2: " + codePart2);

        // 4. DELETE запрос - удалить пользователя
        HttpEntity<Void> deleteEntity = new HttpEntity<>(postHeaders);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(url + "/3", HttpMethod.DELETE, deleteEntity, String.class);
        String codePart3 = deleteResponse.getBody();
        System.out.println("Code part 3: " + codePart3);

        // 5. Объединить части кода
        String finalCode = codePart1 + codePart2 + codePart3;
        System.out.println("Final Code: " + finalCode);
    }
}