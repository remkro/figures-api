package com.geofigeo.figuresapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddShapeRequestDto {
    private String type;
    private List<Double> params;
}
