package com.geofigeo.figuresapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SquareCreatedResponseDto extends ShapeCreatedResponseDto {
    private double width;
}
