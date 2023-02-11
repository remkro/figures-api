package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Shape;

public interface ShapeHandler {
    String getShapeName();

    ShapeCreatedResponseDto save(Shape partialShape, AddShapeRequestDto addShapeDto, String username);
}
