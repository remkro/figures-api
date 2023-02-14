package com.geofigeo.figuresapi.service.handler;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.dto.SquareDto;
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
public class SquareHandler implements ShapeHandler {
    private final ShapeRepository shapeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public String getShapeName() {
        return "SQUARE";
    }

    @Override
    public Map<String, Integer> getParamsNames() {
        Map<String, Integer> map = new HashMap<>();
        map.put("height", 0);
        return map;
    }

    @Transactional
    @Override
    public ShapeDto save(Shape shape, AddShapeRequestDto request, String username) {
        shape.setType(this.getShapeName());
        shape.setArea(calculateArea(request.getParams().get(0)));
        shape.setPerimeter(calculatePerimeter(request.getParams().get(0)));
        Shape persistedSquare = shapeRepository.saveAndFlush(shape);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.addShape(persistedSquare);
        return mapShapeToSpecificDto(persistedSquare);
    }

    @Override
    public Shape edit(Shape shape) {
        shape.setArea(calculateArea(shape.getProperties().get("height")));
        shape.setPerimeter(calculatePerimeter(shape.getProperties().get("height")));
        return shape;
    }

    @Override
    public ShapeDto mapShapeToSpecificDto(Shape shape) {
        SquareDto squareDto = new SquareDto();
        modelMapper.map(shape, squareDto);
        squareDto.setHeight(shape.getProperties().get("height"));
        return squareDto;
    }

    private double calculateArea(double side) {
        return side * side;
    }

    private double calculatePerimeter(double side) {
        return 4 * side;
    }
}
