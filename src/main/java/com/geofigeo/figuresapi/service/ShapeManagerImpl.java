package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Change;
import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.exception.ShapeNotSupportedException;
import com.geofigeo.figuresapi.abstraction.ChangeManager;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.abstraction.ShapeManager;
import com.geofigeo.figuresapi.repository.ChangeRepository;
import com.geofigeo.figuresapi.repository.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShapeManagerImpl implements ShapeManager {
    private final ShapeRepository shapeRepository;
    private final ChangeRepository changeRepository;
    private final List<ShapeHandler> handlers;
    private final ModelMapper modelMapper;
    private final ChangeManager changeManager;
    private static final Logger logger = LoggerFactory.getLogger(ShapeManagerImpl.class);

    @Override
    public ShapeDto save(AddShapeRequestDto addShapeRequestDto, String username) {
        ShapeHandler handler = getHandler(addShapeRequestDto.getType());
        Shape partialShape = createPartialShape(addShapeRequestDto, username);
        logger.info("Adding shape: " + addShapeRequestDto.getType());
        return handler.save(partialShape, addShapeRequestDto, username);
    }

    @Transactional(readOnly = true)
    @Override
    public ShapeDto getSingleShape(long id) {
        if (!shapeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shape not found!");
        }
        Shape shape = shapeRepository.findById(id).get();
        ShapeHandler handler = getHandler(shape.getType());
        return handler.mapShapeToSpecificDto(shape);
    }

    @Transactional
    @Override
    public ShapeDto editSingleShape(EditShapeRequestDto editShapeDto, String username) {
        if (!shapeRepository.existsById(editShapeDto.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shape not found!");
        }

        Shape shape = shapeRepository.findById(editShapeDto.getId()).get();
        List<Double> oldParams = shape.getParams();
        shape.setParams(editShapeDto.getParams());
        shape.setLastModifiedBy(username);
        shape.setLastModifiedAt(LocalDateTime.now());

        ShapeHandler handler = getHandler(shape.getType());
        shape = handler.edit(shape);
        Shape editedShape = shapeRepository.saveAndFlush(shape);
        changeManager.save(editedShape, username, oldParams, handler);

        return handler.mapShapeToSpecificDto(editedShape);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ShapeChangeDto> getShapeChanges(long id) {
        List<Change> changes = changeRepository.getAllByShapeId(id);
        return changes.stream().map(c -> modelMapper.map(c, ShapeChangeDto.class)).toList();
    }

    @Transactional(readOnly = true)
    public List<Shape> getFiltered(String type, Double areaFrom, Double areaTo, Double perimeterFrom, Double perimeterTo) {
        return shapeRepository.getFiltered(type, areaFrom, areaTo, perimeterFrom, perimeterTo);
    }

    @Transactional(readOnly = true)
    public List<Shape> getAll() {
        return shapeRepository.findAll();
    }

    private ShapeHandler getHandler(String shapeName) {
        return handlers.stream().filter(h -> h.getShapeName().equalsIgnoreCase(shapeName)).findFirst()
                .orElseThrow(() -> new ShapeNotSupportedException("Shape not supported! Handler not found!"));
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
