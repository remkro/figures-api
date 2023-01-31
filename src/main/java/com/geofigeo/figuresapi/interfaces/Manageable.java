package com.geofigeo.figuresapi.interfaces;

import com.geofigeo.figuresapi.dtos.AddShapeDto;

public interface Manageable {
    String getShapeName();
    void save(AddShapeDto addShapeDto);
}
