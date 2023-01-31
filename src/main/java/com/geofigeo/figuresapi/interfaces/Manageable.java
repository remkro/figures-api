package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Shape;

import java.time.LocalDateTime;

public interface Manageable {
    String getShapeName();
    ShapeCreatedResponseDto save(AddShapeRequestDto addShapeDto);

    default ShapeCreatedResponseDto createPartialShapeCreatedResponse(Shape shape) {
        ShapeCreatedResponseDto responseDto = new ShapeCreatedResponseDto();
        responseDto.setId(shape.getId());
        responseDto.setType(this.getShapeName());
        responseDto.setCreatedBy("Anonymous");
        responseDto.setCreatedAt(LocalDateTime.now());
        responseDto.setLastModifiedAt(LocalDateTime.now());
        responseDto.setLastModifiedBy("Anonymous");
        return responseDto;
    }
}
