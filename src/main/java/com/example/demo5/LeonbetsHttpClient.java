package com.example.demo5;

import com.example.demo5.model.matches.EnabledDto;
import com.example.demo5.model.matches.EventDto;
import com.example.demo5.model.matches.SportDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeonbetsHttpClient {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public List<SportDto> getSports() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://leonbets.com/api-2/betline/sports?ctag=ru-UA&flags=urlv2"))
                .header("authority", "leonbets.com")
                .header("accept", "*/*")
                .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public EnabledDto getMatches(String leagueId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://leonbets.com/api-2/betline/events/all?ctag=ru-UA&league_id=" + leagueId + "&hideClosed=true&flags=reg,urlv2,mm2,rrc,nodup"))
                .header("authority", "leonbets.com")
                .header("accept", "*/*")
                .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new EnabledDto();
    }

    public EventDto getСofs(String eventId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://leonbets.com/api-2/betline/event/all?ctag=ru-UA&eventId=" + eventId + "&flags=reg,urlv2,mm2,rrc,nodup,smg,outv2"))
                .header("authority", "leonbets.com")
                .header("accept", "*/*")
                .header("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new EventDto();
    }
}
