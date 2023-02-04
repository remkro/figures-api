package com.geofigeo.figuresapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RectangleCreatedResponseDto extends ShapeCreatedResponseDto {
    double width;
    double height;
}
