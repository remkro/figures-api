package com.geofigeo.figuresapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class EditShapeRequestDto {
    private long id;
    private List<Double> params;
}
