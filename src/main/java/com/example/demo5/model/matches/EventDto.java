package com.example.demo5.model.matches;

import lombok.Data;

import java.util.List;

@Data
public class EventDto {
    private String id;
    private String name;
    private Long kickoff;
    private LeagueDto league;
    private List<MarketDto> markets;
}
