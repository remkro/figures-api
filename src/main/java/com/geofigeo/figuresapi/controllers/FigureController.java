package com.geofigeo.figuresapi.controllers;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeCreatedResponseDto;
import com.geofigeo.figuresapi.interfaces.ShapeManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shapes")
@RequiredArgsConstructor
public class FigureController {
    private final ShapeManager shapeManager;

    @PostMapping
    public ResponseEntity<ShapeCreatedResponseDto> add(@RequestBody AddShapeRequestDto addShapeRequestDto   ) {
        ShapeCreatedResponseDto shapeSaveResponse = shapeManager.save(addShapeRequestDto);
        return ResponseEntity.ok(shapeSaveResponse);
    }
}
