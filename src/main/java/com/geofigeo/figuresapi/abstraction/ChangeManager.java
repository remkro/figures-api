package com.geofigeo.figuresapi.abstraction;

import com.geofigeo.figuresapi.entity.Shape;

import java.util.List;

public interface ChangeManager {
    void save(Shape editedShape, String username, List<Double> oldParams, ShapeHandler handler);
}
