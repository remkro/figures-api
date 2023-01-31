package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;

public interface ShapeManager {
    public ShapeCreatedResponseDto save(AddShapeRequestDto addShapeDto);
}
