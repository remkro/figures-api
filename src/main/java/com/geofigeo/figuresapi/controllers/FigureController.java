package com.geofigeo.figuresapi.controllers;

import com.geofigeo.figuresapi.dtos.AddShapeRequestDto;
import com.geofigeo.figuresapi.dtos.EditShapeRequestDto;
import com.geofigeo.figuresapi.dtos.ShapeChangeDto;
import com.geofigeo.figuresapi.dtos.ShapeDto;
import com.geofigeo.figuresapi.entities.Shape;
import com.geofigeo.figuresapi.interfaces.ShapeManager;
import com.geofigeo.figuresapi.security.UserSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shapes")
@RequiredArgsConstructor
public class FigureController {
    private final ShapeManager shapeManager;
    private final UserSecurity userSecurity;

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<ShapeDto> add(@RequestBody AddShapeRequestDto addShapeRequestDto,
                                        Principal principal) {
        ShapeDto responseDto = shapeManager.save(addShapeRequestDto, principal.getName());
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

    @GetMapping("/{id}")
    public ResponseEntity<ShapeDto> getSingle(@PathVariable long id) {
        return ResponseEntity.ok(shapeManager.getSingleShape(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.isResourceCreator(#editShapeRequestDto.id)")
    @PutMapping
    public ResponseEntity<ShapeDto> edit(@RequestBody EditShapeRequestDto editShapeRequestDto,
                                         Principal principal) {
        ShapeDto response = shapeManager.editSingleShape(editShapeRequestDto, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.isResourceCreator(#id)")
    @GetMapping("/{id}/changes")
    public ResponseEntity<List<ShapeChangeDto>> getChanges(@PathVariable long id) {
        List<ShapeChangeDto> shapeChanges = shapeManager.getShapeChanges(id);
        return ResponseEntity.ok(shapeChanges);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Shape>> getAll() {
        return ResponseEntity.ok(shapeManager.getAll());
    }
}
