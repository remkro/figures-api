package com.geofigeo.figuresapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddShapeRequestDto {
    private String type;
    private List<Double> params;
}
