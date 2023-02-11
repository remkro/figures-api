package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Shape;
import com.geofigeo.figuresapi.exceptions.ShapeNotSupportedException;
import com.geofigeo.figuresapi.interfaces.ShapeHandler;
import com.geofigeo.figuresapi.interfaces.ShapeManager;
import com.geofigeo.figuresapi.repositories.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShapeManagerImpl implements ShapeManager {
    private final ShapeRepository shapeRepository;
    private final List<ShapeHandler> handlers;
    private static final Logger logger = LoggerFactory.getLogger(ShapeManagerImpl.class);

    @Override
    public ShapeCreatedResponseDto save(AddShapeRequestDto addShapeRequestDto, String username) {
        String shapeName = null;
        ShapeCreatedResponseDto responseDto = null;
        for (ShapeHandler handler : handlers) {
            if(addShapeRequestDto.getType().equalsIgnoreCase(handler.getShapeName())) {
                shapeName = handler.getShapeName();
                Shape partialShape = createPartialShape(addShapeRequestDto, username);
                logger.info("Adding shape: " + shapeName);
                responseDto = handler.save(partialShape, addShapeRequestDto, username);
            }
        }

        if(shapeName == null)
            throw new ShapeNotSupportedException("Shape not supported!");

        return responseDto;
    }

    @Transactional
    public List<Shape> getFiltered(String type, Double areaFrom, Double areaTo, Double perimeterFrom, Double perimeterTo) {
        return shapeRepository.getFiltered(type, areaFrom, areaTo, perimeterFrom, perimeterTo);
    }

    @Transactional(readOnly = true)
    public List<Shape> getAll() {
        return shapeRepository.findAll();
    }

    private Shape createPartialShape(AddShapeRequestDto addShapeRequestDto, String username) {
        Shape shape = new Shape();
        shape.setParams(addShapeRequestDto.getParams());
        shape.setCreatedBy(username);
        shape.setCreatedAt(LocalDateTime.now());
        shape.setLastModifiedAt(LocalDateTime.now());
        shape.setLastModifiedBy(username);
        return shape;
    }
}
