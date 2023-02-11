package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.RectangleCreatedResponseDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
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

import java.util.Optional;

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

    @Transactional
    @Override
    public ShapeCreatedResponseDto save(Shape shape, AddShapeRequestDto addShapeRequestDto, String username) {
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
        return mapRectangleToShapeCreatedResponseDto(persistedRectangle);
    }

    private ShapeCreatedResponseDto mapRectangleToShapeCreatedResponseDto(Shape rectangle) {
        RectangleCreatedResponseDto responseDto = new RectangleCreatedResponseDto();
        modelMapper.map(rectangle, responseDto);
        responseDto.setWidth(rectangle.getParams().get(0));
        responseDto.setHeight(rectangle.getParams().get(1));
        return responseDto;
    }

    private double calculateArea(double sideA, double sideB) {
        return sideA * sideB;
    }

    private double calculatePerimeter(double sideA, double sideB) {
        return 4 * (sideA + sideB);
    }
}
