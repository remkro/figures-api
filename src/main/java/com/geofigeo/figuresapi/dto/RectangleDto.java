package com.geofigeo.figuresapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RectangleDto extends ShapeDto {
    double width;
    double height;
}
