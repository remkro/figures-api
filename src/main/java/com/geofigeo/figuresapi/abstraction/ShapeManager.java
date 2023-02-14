package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Shape;

import java.util.List;
import java.util.Map;

public interface ShapeManager {
    ShapeDto save(AddShapeRequestDto addShapeDto, String username);
    List<Shape> getAll();
    List<ShapeDto> getFilteredShapes(Map<String, String> searchParams);
    ShapeDto getSingleShape(long id);
    ShapeDto editSingleShape(EditShapeRequestDto editShapeDto, String username);
    List<ShapeChangeDto> getShapeChanges(long id);
}
