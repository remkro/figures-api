package com.geofigeo.figuresapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CircleCreatedResponseDto extends ShapeCreatedResponseDto {
    private double radius;
}
