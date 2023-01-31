package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Rectangle;
import com.geofigeo.figuresapi.interfaces.Manageable;
import com.geofigeo.figuresapi.repositories.RectangleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RectangleManager implements Manageable {
    private final RectangleRepository rectangleRepository;

    @Override
    public String getShapeName() {
        return "RECTANGLE";
    }

    @Override
    public ShapeCreatedResponseDto save(AddShapeRequestDto addShapeRequestDto) {
        Double sideA = addShapeRequestDto.getParams().get(0);
        Double sideB = addShapeRequestDto.getParams().get(1);
        Rectangle rectangle = new Rectangle(sideA, sideB);
        Rectangle persistedRectangle = rectangleRepository.saveAndFlush(rectangle);
        return mapRectangleToShapeCreatedResponseDto(persistedRectangle);
    }

    private ShapeCreatedResponseDto mapRectangleToShapeCreatedResponseDto(Rectangle rectangle) {
        ShapeCreatedResponseDto responseDto = createPartialShapeCreatedResponse(rectangle);
        responseDto.setArea(calculateArea(rectangle.getSideA(), rectangle.getSideB()));
        responseDto.setPerimeter(calculatePerimeter(rectangle.getSideA(), rectangle.getSideB()));
        return responseDto;
    }

    private double calculateArea(double sideA, double sideB) {
        return sideA * sideB;
    }

    private double calculatePerimeter(double sideA, double sideB) {
        return 4 * (sideA + sideB);
    }
}
