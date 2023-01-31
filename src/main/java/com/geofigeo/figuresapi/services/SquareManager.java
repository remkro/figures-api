package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Square;
import com.geofigeo.figuresapi.interfaces.Manageable;
import com.geofigeo.figuresapi.repositories.SquareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SquareManager implements Manageable {
    private final SquareRepository squareRepository;

    @Override
    public String getShapeName() {
        return "SQUARE";
    }

    @Override
    public ShapeCreatedResponseDto save(AddShapeRequestDto addShapeRequestDto) {
        Square square = new Square(addShapeRequestDto.getParams().get(0));
        Square persistedSquare = squareRepository.saveAndFlush(square);
        return mapSquareToShapeCreatedResponseDto(persistedSquare);
    }

    private ShapeCreatedResponseDto mapSquareToShapeCreatedResponseDto(Square square) {
        ShapeCreatedResponseDto responseDto = createPartialShapeCreatedResponse(square);
        responseDto.setArea(calculateArea(square.getSide()));
        responseDto.setPerimeter(calculatePerimeter(square.getSide()));
        return responseDto;
    }

    private double calculateArea(double side) {
        return side * side;
    }

    private double calculatePerimeter(double side) {
        return 4 * side;
    }
}
