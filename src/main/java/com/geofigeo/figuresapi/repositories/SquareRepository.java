package com.geofigeo.figuresapi.repositories;

import com.geofigeo.figuresapi.entities.Shape;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SquareRepository extends JpaRepository<Shape, Long> {
}
