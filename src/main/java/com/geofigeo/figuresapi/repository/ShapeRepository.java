package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Shape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShapeRepository extends JpaRepository<Shape, Long> {
    @Query("SELECT s.type FROM Shape s WHERE s.id = ?1")
    Optional<String> getTypeById(long id);
}
