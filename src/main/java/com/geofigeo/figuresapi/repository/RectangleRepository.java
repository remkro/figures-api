package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Rectangle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RectangleRepository extends JpaRepository<Rectangle, Long> {
}
