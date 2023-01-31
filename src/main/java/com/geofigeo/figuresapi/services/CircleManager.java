package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Circle;
import com.geofigeo.figuresapi.interfaces.Manageable;
import com.geofigeo.figuresapi.repositories.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CircleManager implements Manageable {
    private final CircleRepository circleRepository;

    @Override
    public String getShapeName() {
        return "CIRCLE";
    }

    @Override
    public ShapeCreatedResponseDto save(AddShapeRequestDto addShapeRequestDto) {
        Circle circle = new Circle(addShapeRequestDto.getParams().get(0));
        Circle persistedCircle = circleRepository.saveAndFlush(circle);
        return mapCircleToShapeCreatedResponseDto(persistedCircle);
    }

    private ShapeCreatedResponseDto mapCircleToShapeCreatedResponseDto(Circle circle) {
        ShapeCreatedResponseDto responseDto = createPartialShapeCreatedResponse(circle);
        responseDto.setArea(calculateArea(circle.getRadius()));
        responseDto.setPerimeter(calculatePerimeter(circle.getRadius()));
        return responseDto;
    }

    private double calculateArea(double radius) {
        return Math.PI * (Math.pow(radius, 2));
    }

    private double calculatePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }
}
