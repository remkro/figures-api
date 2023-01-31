package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeDto;
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
    public void save(AddShapeDto addShapeDto) {
        Square square = new Square(addShapeDto.getParams().get(0));
        squareRepository.save(square);
    }
}
