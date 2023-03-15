package com.geofigeo.figuresapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EditShapeRequestDto {
    private long id;
    private List<Double> params;
}
