package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Shape;

import java.util.Map;

public interface ShapeHandler {
    String getShapeName();
    Map<String, Integer> getParamsNames();
    ShapeDto save(Shape partialShape, AddShapeRequestDto addShapeDto, String username);
    Shape edit(Shape shape);
    ShapeDto mapShapeToSpecificDto(Shape shape);
}
