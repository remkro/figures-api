package com.geofigeo.figuresapi.repositories;

import com.geofigeo.figuresapi.entities.Rectangle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RectangleRepository extends JpaRepository<Rectangle, Long> {
}
