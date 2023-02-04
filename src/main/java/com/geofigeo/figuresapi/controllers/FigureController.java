package com.geofigeo.figuresapi.controllers;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.entities.Shape;
import com.geofigeo.figuresapi.interfaces.ShapeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shapes")
@RequiredArgsConstructor
public class FigureController {
    private final ShapeManager shapeManager;

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<ShapeCreatedResponseDto> add(@RequestBody AddShapeRequestDto addShapeRequestDto) {
        ShapeCreatedResponseDto responseDto = shapeManager.save(addShapeRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping()
    public ResponseEntity<List<Shape>> getFiltered(@RequestParam(required = false) String type,
                                                   @RequestParam(required = false) Double areaFrom,
                                                   @RequestParam(required = false) Double areaTo,
                                                   @RequestParam(required = false) Double perimeterFrom,
                                                   @RequestParam(required = false) Double perimeterTo) {
        return ResponseEntity.ok(shapeManager.getFiltered(type, areaFrom, areaTo, perimeterFrom, perimeterTo));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Shape>> getAll() {
        return ResponseEntity.ok(shapeManager.getAll());
    }
}
