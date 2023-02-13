package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.RectangleDto;
import com.geofigeo.figuresapi.dtos.ShapeDto;
import com.geofigeo.figuresapi.entities.Shape;
import com.geofigeo.figuresapi.entities.User;
import com.geofigeo.figuresapi.interfaces.ShapeHandler;
import com.geofigeo.figuresapi.repositories.ShapeRepository;
import com.geofigeo.figuresapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RectangleHandler implements ShapeHandler {
    private final ShapeRepository shapeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public String getShapeName() {
        return "RECTANGLE";
    }

    @Override
    public Map<String, Integer> getParamsNames() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Width", 0);
        map.put("Height", 1);
        return map;
    }

    @Transactional
    @Override
    public ShapeDto save(Shape shape, AddShapeRequestDto addShapeRequestDto, String username) {
        shape.setType(getShapeName());
        shape.setArea(calculateArea(
                addShapeRequestDto.getParams().get(0),
                addShapeRequestDto.getParams().get(1)
        ));
        shape.setPerimeter(calculatePerimeter(
                addShapeRequestDto.getParams().get(0),
                addShapeRequestDto.getParams().get(1)
        ));
        Shape persistedRectangle = shapeRepository.saveAndFlush(shape);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.addShape(persistedRectangle);
        return mapShapeToSpecificDto(persistedRectangle);
    }

    @Override
    public Shape edit(Shape shape) {
        shape.setArea(calculateArea(
                shape.getParams().get(0),
                shape.getParams().get(1)
        ));
        shape.setPerimeter(calculatePerimeter(
                shape.getParams().get(0),
                shape.getParams().get(1)
        ));
        return shape;
    }

    @Override
    public ShapeDto mapShapeToSpecificDto(Shape shape) {
        RectangleDto rectangleDto = new RectangleDto();
        modelMapper.map(shape, rectangleDto);
        rectangleDto.setWidth(shape.getParams().get(0));
        rectangleDto.setHeight(shape.getParams().get(1));
        return rectangleDto;
    }

    private double calculateArea(double sideA, double sideB) {
        return sideA * sideB;
    }

    private double calculatePerimeter(double sideA, double sideB) {
        return 2 * (sideA + sideB);
    }
}
