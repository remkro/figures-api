package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Shape;

import java.util.List;

public interface ShapeManager {
    ShapeCreatedResponseDto save(AddShapeRequestDto addShapeDto, String username);
    List<Shape> getAll();
    List<Shape> getFiltered(String type, Double areaFrom, Double areaTo, Double perimeterFrom, Double perimeterTo);
}
