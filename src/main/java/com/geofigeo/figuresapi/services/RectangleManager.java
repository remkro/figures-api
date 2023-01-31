package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeDto;
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
    public void save(AddShapeDto addShapeDto) {
        Double sideA = addShapeDto.getParams().get(0);
        Double sideB = addShapeDto.getParams().get(1);
        Rectangle rectangle = new Rectangle(sideA, sideB);
        rectangleRepository.save(rectangle);
    }
}
