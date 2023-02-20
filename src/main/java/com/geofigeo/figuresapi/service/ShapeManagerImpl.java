package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.exception.ShapeNotSupportedException;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.abstraction.ShapeManager;
import com.geofigeo.figuresapi.repository.ShapeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShapeManagerImpl implements ShapeManager {
    private final ShapeRepository shapeRepository;
    private final List<ShapeHandler> handlers;

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
        List<ShapeDto> shapes = new ArrayList<>();
        for (ShapeHandler handler : handlers) {
            shapes.addAll(handler.getFiltered(searchParams));
        }
        if (searchParams.get("areaFrom") != null) {
            shapes = shapes.stream()
                    .filter(s -> s.getArea() >= Double.parseDouble(searchParams.get("areaFrom"))).toList();
        }
        if (searchParams.get("areaTo") != null) {
            shapes = shapes.stream()
                    .filter(s -> s.getArea() <= Double.parseDouble(searchParams.get("areaFrom"))).toList();
        }
        if (searchParams.get("perimeterFrom") != null) {
            shapes = shapes.stream()
                    .filter(s -> s.getPerimeter() >= Double.parseDouble(searchParams.get("perimeterFrom"))).toList();
        }
        if (searchParams.get("perimeterTo") != null) {
            shapes = shapes.stream()
                    .filter(s -> s.getPerimeter() <= Double.parseDouble(searchParams.get("perimeterTo"))).toList();
        }
        return shapes;
    }

    private ShapeHandler getHandler(String type) {
        return handlers.stream().filter(h -> h.getShapeName().equalsIgnoreCase(type)).findFirst()
                .orElseThrow(() -> new ShapeNotSupportedException("HANDLER_NOT_FOUND"));
    }

    private ShapeHandler getHandler(long shapeId) {
        String type = shapeRepository.getTypeById(shapeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SHAPE_NOT_FOUND"));
        return getHandler(type);
    }
}
