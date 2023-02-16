package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.exception.ShapeNotSupportedException;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.abstraction.ShapeManager;
import com.geofigeo.figuresapi.repository.ShapeRepository;
import com.geofigeo.figuresapi.service.util.ComplexSearchPerformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShapeManagerImpl implements ShapeManager {
    private final ShapeRepository shapeRepository;
    private final List<ShapeHandler> handlers;
    private final ModelMapper modelMapper;
    private final ComplexSearchPerformer performer;

    @Override
    public ShapeDto saveShape(AddShapeRequestDto addShapeRequestDto, String username) {
        ShapeHandler handler = getHandler(addShapeRequestDto.getType());
        log.info("Adding shape: " + addShapeRequestDto.getType().toUpperCase());
        return handler.save(addShapeRequestDto, username);
    }

    @Override
    public ShapeDto getSingleShape(long id) {
        ShapeHandler handler = getHandler(id);
        return handler.getSingle(id);
    }

    @Override
    public ShapeDto editShape(EditShapeRequestDto request, String username) {
        ShapeHandler handler = getHandler(request.getId());
        return handler.edit(request, username);
    }

    @Override
    public List<ShapeChangeDto> getShapeChanges(long id) {
         ShapeHandler handler = getHandler(id);
         return handler.getChanges(id);
    }

    @Override
    public List<ShapeDto> getFilteredShapes(Map<String, String> searchParams) {
        List<Shape> result = performer.search(searchParams, handlers);
        return result.stream().map(s -> modelMapper.map(s, ShapeDto.class)).toList();
    }

    private ShapeHandler getHandler(String type) {
        return handlers.stream().filter(h -> h.getShapeName().equalsIgnoreCase(type)).findFirst()
                .orElseThrow(() -> new ShapeNotSupportedException("Shape not supported! Handler not found!"));
    }

    private ShapeHandler getHandler(long shapeId) {
        String type = shapeRepository.getTypeById(shapeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shape not found!"));
        return getHandler(type);
    }
}
