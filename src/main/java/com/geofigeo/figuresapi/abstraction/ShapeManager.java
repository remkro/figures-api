package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.*;
import com.geofigeo.figuresapi.entity.Shape;

import java.util.List;

public interface ShapeManager {
    ShapeDto save(AddShapeRequestDto addShapeDto, String username);
    List<Shape> getAll();
    List<Shape> getFiltered(String type, Double areaFrom, Double areaTo, Double perimeterFrom, Double perimeterTo);
    ShapeDto getSingleShape(long id);
    ShapeDto editSingleShape(EditShapeRequestDto editShapeDto, String username);
    List<ShapeChangeDto> getShapeChanges(long id);
}
