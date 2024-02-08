package com.example.demo5.model.matches;

import lombok.Data;

@Data
public class LeagueDto {
    private String id;
    private String name;
    private Boolean top;
    private MatchSportDto sport;
}
