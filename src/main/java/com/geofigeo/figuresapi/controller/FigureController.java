package com.geofigeo.figuresapi.controller;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.abstraction.ShapeManager;
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
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/shapes")
@RequiredArgsConstructor
public class FigureController {
    private final ShapeManager shapeManager;

    @PreAuthorize("hasRole('ROLE_CREATOR')")
    @PostMapping
    public ResponseEntity<ShapeDto> add(@RequestBody AddShapeRequestDto addShapeRequestDto,
                                        Principal principal) {
        ShapeDto responseDto = shapeManager.saveShape(addShapeRequestDto, principal.getName());
        return new ResponseEntity<>(responseDto, CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<ShapeDto>> getFiltered(@RequestParam Map<String, String> searchParams) {
        List<ShapeDto> filtered = shapeManager.getFilteredShapes(searchParams);
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShapeDto> getSingle(@PathVariable long id) {
        return ResponseEntity.ok(shapeManager.getSingleShape(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.isResourceCreator(#editShapeRequestDto.id)")
    @PutMapping
    public ResponseEntity<ShapeDto> edit(@RequestBody EditShapeRequestDto editShapeRequestDto,
                                         Principal principal) {
        ShapeDto response = shapeManager.editShape(editShapeRequestDto, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userSecurity.isResourceCreator(#id)")
    @GetMapping("/{id}/changes")
    public ResponseEntity<List<ShapeChangeDto>> getChanges(@PathVariable long id) {
        List<ShapeChangeDto> shapeChanges = shapeManager.getShapeChanges(id);
        return ResponseEntity.ok(shapeChanges);
    }
}
