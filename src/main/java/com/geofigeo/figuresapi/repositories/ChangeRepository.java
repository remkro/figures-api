package com.geofigeo.figuresapi.repositories;

import com.geofigeo.figuresapi.entities.Change;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeRepository extends JpaRepository<Change, Long> {
    List<Change> getAllByShapeId(long shapeId);
}
