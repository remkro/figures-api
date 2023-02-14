package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.entity.Shape;

import java.util.Map;

public interface ChangeManager {
    void save(Shape editedShape, String username, Map<String, Double> oldProperties, ShapeHandler handler);
}
