package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;

import java.util.List;
import java.util.Map;

public interface ShapeManager {
    ShapeDto saveShape(AddShapeRequestDto addShapeDto, String username);
    List<ShapeDto> getFilteredShapes(Map<String, String> searchParams);
    ShapeDto getSingleShape(long id);
    ShapeDto editShape(EditShapeRequestDto editShapeDto, String username);
    List<ShapeChangeDto> getShapeChanges(long id);
}
