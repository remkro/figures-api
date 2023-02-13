package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.CircleDto;
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
public class CircleHandler implements ShapeHandler {
    private final ShapeRepository shapeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public String getShapeName() {
        return "CIRCLE";
    }

    @Override
    public Map<String, Integer> getParamsNames() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Radius", 0);
        return map;
    }

    @Transactional
    @Override
    public ShapeDto save(Shape shape, AddShapeRequestDto request, String username) {
        shape.setType(getShapeName());
        shape.setArea(calculateArea(request.getParams().get(0)));
        shape.setPerimeter(calculatePerimeter(request.getParams().get(0)));
        Shape persistedCircle = shapeRepository.saveAndFlush(shape);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.addShape(persistedCircle);
        return mapShapeToSpecificDto(persistedCircle);
    }

    @Override
    public Shape edit(Shape shape) {
        shape.setArea(calculateArea(shape.getParams().get(0)));
        shape.setPerimeter(calculatePerimeter(shape.getParams().get(0)));
        return shape;
    }

    @Override
    public ShapeDto mapShapeToSpecificDto(Shape shape) {
        CircleDto circleDto = new CircleDto();
        modelMapper.map(shape, circleDto);
        circleDto.setRadius(shape.getParams().get(0));
        return circleDto;
    }

    private double calculateArea(double radius) {
        return Math.PI * (Math.pow(radius, 2));
    }

    private double calculatePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }
}
