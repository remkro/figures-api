package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.entities.Shape;

import java.util.List;

public interface ChangeManager {
    void save(Shape editedShape, String username, List<Double> oldParams, ShapeHandler handler);
}
