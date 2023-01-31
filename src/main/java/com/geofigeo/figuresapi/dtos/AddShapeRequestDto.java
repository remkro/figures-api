package com.geofigeo.figuresapi.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AddShapeRequestDto {
    private String type;
    private List<Double> params;
}
