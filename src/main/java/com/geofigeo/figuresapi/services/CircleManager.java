package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeDto;
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
    public void save(AddShapeDto addShapeDto) {
        Circle circle = new Circle(addShapeDto.getParams().get(0));
        circleRepository.save(circle);
    }
}
