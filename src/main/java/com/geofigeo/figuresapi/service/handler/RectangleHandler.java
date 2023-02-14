package com.geofigeo.figuresapi.service.handler;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.RectangleDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.repository.ShapeRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
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
        map.put("width", 0);
        map.put("height", 1);
        return map;
    }

    @Transactional
    @Override
    public ShapeDto save(Shape shape, AddShapeRequestDto request, String username) {
        shape.setType(getShapeName());
        shape.setArea(calculateArea(
                request.getParams().get(0),
                request.getParams().get(1)
        ));
        shape.setPerimeter(calculatePerimeter(
                request.getParams().get(0),
                request.getParams().get(1)
        ));
        getParamsNames().forEach((key, value) -> shape.addProperty(key, request.getParams().get(value)));
        Shape persistedRectangle = shapeRepository.saveAndFlush(shape);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.addShape(persistedRectangle);
        return mapShapeToSpecificDto(persistedRectangle);
    }

    @Override
    public Shape edit(Shape shape) {
        shape.setArea(calculateArea(
                shape.getProperties().get("width"),
                shape.getProperties().get("height")
        ));
        shape.setPerimeter(calculatePerimeter(
                shape.getProperties().get("width"),
                shape.getProperties().get("height")
        ));
        return shape;
    }

    @Override
    public ShapeDto mapShapeToSpecificDto(Shape shape) {
        RectangleDto rectangleDto = new RectangleDto();
        modelMapper.map(shape, rectangleDto);
        rectangleDto.setWidth(shape.getProperties().get("width"));
        rectangleDto.setHeight(shape.getProperties().get("height"));
        return rectangleDto;
    }

    private double calculateArea(double sideA, double sideB) {
        return sideA * sideB;
    }

    private double calculatePerimeter(double sideA, double sideB) {
        return 2 * (sideA + sideB);
    }
}
