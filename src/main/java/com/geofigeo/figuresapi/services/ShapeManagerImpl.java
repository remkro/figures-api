package com.geofigeo.figuresapi.services;

import com.geofigeo.figuresapi.dtos.AddShapeDto;
import com.geofigeo.figuresapi.exceptions.ShapeNotSupportedException;
import com.geofigeo.figuresapi.interfaces.Manageable;
import com.geofigeo.figuresapi.interfaces.ShapeManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShapeManagerImpl implements ShapeManager {
    private final List<Manageable> managers;
    private static final Logger logger = LoggerFactory.getLogger(ShapeManagerImpl.class);

    @Override
    public void save(AddShapeDto addShapeDto) {
        String shape = null;
        for (Manageable manager : managers) {
            if(addShapeDto.getType().equalsIgnoreCase(manager.getShapeName())) {
                shape = manager.getShapeName();
                logger.info("Adding shape: " + shape);
                manager.save(addShapeDto);
            }
        }

        if(shape == null)
            throw new ShapeNotSupportedException("Shape not supported!");
    }
}
