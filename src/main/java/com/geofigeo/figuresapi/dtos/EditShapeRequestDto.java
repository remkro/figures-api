package com.geofigeo.figuresapi.dtos;

import lombok.Data;

import java.util.List;

@Data
public class EditShapeRequestDto {
    private long id;
    private List<Double> params;
}
