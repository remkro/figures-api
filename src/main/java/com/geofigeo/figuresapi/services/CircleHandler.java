package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.CircleCreatedResponseDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Shape;
import com.geofigeo.figuresapi.interfaces.ShapeHandler;
import com.geofigeo.figuresapi.repositories.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CircleHandler implements ShapeHandler {
    private final ShapeRepository shapeRepository;
    private final ModelMapper modelMapper;

    @Override
    public String getShapeName() {
        return "CIRCLE";
    }

    @Transactional
    @Override
    public ShapeCreatedResponseDto save(Shape shape, AddShapeRequestDto request) {
        shape.setType(getShapeName());
        shape.setArea(calculateArea(request.getParams().get(0)));
        shape.setPerimeter(calculatePerimeter(request.getParams().get(0)));
        Shape persistedCircle = shapeRepository.saveAndFlush(shape);
        return mapCircleToShapeCreatedResponseDto(persistedCircle);
    }

    private ShapeCreatedResponseDto mapCircleToShapeCreatedResponseDto(Shape circle) {
        CircleCreatedResponseDto responseDto = new CircleCreatedResponseDto();
        modelMapper.map(circle, responseDto);
        responseDto.setRadius(circle.getParams().get(0));
        return responseDto;
    }

    private double calculateArea(double radius) {
        return Math.PI * (Math.pow(radius, 2));
    }

    private double calculatePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }
}
