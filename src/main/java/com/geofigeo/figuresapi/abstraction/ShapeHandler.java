package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;

import java.util.List;
import java.util.Map;

public interface ShapeHandler {
    String getShapeName();
    Map<String, Integer> getParamsNames();
    ShapeDto save(AddShapeRequestDto addShapeDto, String username);
    ShapeDto getSingle(long id);
    ShapeDto edit(EditShapeRequestDto request, String username);
    List<ShapeChangeDto> getChanges(long id);
    List<ShapeDto> getFiltered(Map<String, String> searchParams);
}
