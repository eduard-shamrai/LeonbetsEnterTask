package com.example.demo5.model.matches;

import lombok.Data;

import java.util.List;

@Data
public class RegionDto {
    private String id;
    private List<LeagueDto> leagues;
}
