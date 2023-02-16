package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.entity.Shape;

import java.util.List;
import java.util.Map;

public interface ChangeManager {
    void save(Shape editedShape, String username, Map<String, Double> oldProperties);
    List<ShapeChangeDto> getChanges(long shapeId);
}
