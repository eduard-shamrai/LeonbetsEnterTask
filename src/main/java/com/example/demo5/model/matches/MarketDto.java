package com.example.demo5.model.matches;

import lombok.Data;

import java.util.List;

@Data
public class MarketDto {
    String name;
    List<RunnersDto> runners;
}
