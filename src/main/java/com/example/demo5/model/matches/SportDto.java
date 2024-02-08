package com.example.demo5.model.matches;

import lombok.Data;

import java.util.List;

@Data
public class SportDto {
    private String name;
    private String id;
    private List<RegionDto> regions;
}
