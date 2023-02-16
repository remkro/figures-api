package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Change;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChangeRepository extends JpaRepository<Change, Long> {
    List<Change> findAllByShapeId(long shapeId);
}
