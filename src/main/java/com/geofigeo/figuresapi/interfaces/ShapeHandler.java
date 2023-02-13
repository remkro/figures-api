package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeDto;
import com.geofigeo.figuresapi.entities.Shape;

import java.util.Map;

public interface ShapeHandler {
    String getShapeName();
    Map<String, Integer> getParamsNames();
    ShapeDto save(Shape partialShape, AddShapeRequestDto addShapeDto, String username);
    Shape edit(Shape shape);
    ShapeDto mapShapeToSpecificDto(Shape shape);
}
