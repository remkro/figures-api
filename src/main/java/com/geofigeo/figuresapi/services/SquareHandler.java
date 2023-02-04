package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.dtos.SquareCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Shape;
import com.geofigeo.figuresapi.interfaces.ShapeHandler;
import com.geofigeo.figuresapi.repositories.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SquareHandler implements ShapeHandler {
    private final ShapeRepository shapeRepository;
    private final ModelMapper modelMapper;

    @Override
    public String getShapeName() {
        return "SQUARE";
    }

    @Transactional
    @Override
    public ShapeCreatedResponseDto save(Shape shape, AddShapeRequestDto addShapeRequestDto) {
        shape.setType(this.getShapeName());
        shape.setArea(calculateArea(shape.getParams().get(0)));
        shape.setPerimeter(calculatePerimeter(shape.getParams().get(0)));
        Shape persistedSquare = shapeRepository.saveAndFlush(shape);
        return mapSquareToShapeCreatedResponseDto(persistedSquare);
    }

    private ShapeCreatedResponseDto mapSquareToShapeCreatedResponseDto(Shape square) {
        SquareCreatedResponseDto responseDto = new SquareCreatedResponseDto();
        modelMapper.map(square, responseDto);
        responseDto.setWidth(square.getParams().get(0));
        return responseDto;
    }

    private double calculateArea(double side) {
        return side * side;
    }

    private double calculatePerimeter(double side) {
        return 4 * side;
    }
}
