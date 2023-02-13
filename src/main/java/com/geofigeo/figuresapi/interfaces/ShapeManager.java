package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.*;
import com.geofigeo.figuresapi.entities.Change;
import com.geofigeo.figuresapi.entities.Shape;

import java.util.List;

public interface ShapeManager {
    ShapeDto save(AddShapeRequestDto addShapeDto, String username);
    List<Shape> getAll();
    List<Shape> getFiltered(String type, Double areaFrom, Double areaTo, Double perimeterFrom, Double perimeterTo);
    ShapeDto getSingleShape(long id);
    ShapeDto editSingleShape(EditShapeRequestDto editShapeDto, String username);
    List<ShapeChangeDto> getShapeChanges(long id);
}
