package com.example.demo5;

import com.example.demo5.model.matches.EnabledDto;
import com.example.demo5.model.matches.EventDto;
import com.example.demo5.model.matches.LeagueDto;
import com.example.demo5.model.matches.MarketDto;
import com.example.demo5.model.matches.RegionDto;
import com.example.demo5.model.matches.SportDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ParsingService {
    @Value("${http-client.threads-number}")
    private Integer THREADS_NUMBER;

    private final LeonbetsHttpClient leonbetsHttpClient;

    private Executor executor;

    public ParsingService(LeonbetsHttpClient leonbetsHttpClient) {
        this.leonbetsHttpClient = leonbetsHttpClient;
    }

    @PostConstruct
    public void init() {
        this.executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        parse();
    }

    public void parse() {
        List<SportDto> sportDtos = leonbetsHttpClient.getSports();

        sportDtos.stream()
                .map(this::getTopLeagues)
                .flatMap(Collection::stream)
                .map(this::parseMatches)
                .map(CompletableFuture::join)
                .map(EventDto.class::cast)
                .forEach(event -> parseCofs(event.getId()));
    }

    private CompletableFuture<Object> parseMatches(String leagueId) {
        return CompletableFuture.supplyAsync(() -> {

            EnabledDto enabled = leonbetsHttpClient.getMatches(leagueId);

            return enabled.getEvents().stream()
                    .filter(event -> (event.getMarkets() != null))
                    .min(Comparator.comparing(event -> Instant.ofEpochMilli(event.getKickoff())))
                    .orElse(new EventDto());
        }, executor);
    }

    private void parseCofs(String eventId) {
        CompletableFuture.supplyAsync(() -> {

            EventDto event = leonbetsHttpClient.getСofs(eventId);

            if ((event.getName() != null) && (event.getMarkets() != null)) {
                printInfo(event);
            }
            return new EventDto();
        }, executor);
    }

    private void printInfo(EventDto eventDto) {
        System.out.println(eventDto.getLeague().getSport().getName() + "," + eventDto.getLeague().getName());
        System.out.println("\t" + eventDto.getName() + "," + Instant.ofEpochMilli(eventDto.getKickoff()) + "," + eventDto.getId());

        List<MarketDto> markets = eventDto.getMarkets();
        markets.forEach(market -> {

            System.out.println("\t\t" + market.getName());

            market.getRunners().forEach(runner ->
                    System.out.println("\t\t\t" + runner.getName() + ", " + runner.getPrice() + ", " + runner.getId())

            );
        });
    }

    private List<String> getTopLeagues(SportDto sportDto) {
        return sportDto.getRegions().stream()
                .map(RegionDto::getLeagues)
                .flatMap(Collection::stream)
                .filter(LeagueDto::getTop)
                .map(LeagueDto::getId)
                .collect(Collectors.toList());
    }
}
