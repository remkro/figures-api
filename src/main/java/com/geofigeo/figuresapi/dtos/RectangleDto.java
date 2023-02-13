package com.geofigeo.figuresapi.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RectangleDto extends ShapeDto {
    double width;
    double height;
}
