package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Rectangle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RectangleRepository extends JpaRepository<Rectangle, Long> {
    @Query("SELECT c FROM Rectangle c WHERE (?1 IS NULL OR c.type = ?1) AND (?2 IS NULL OR c.createdBy = ?2)" +
            "AND (?3 IS NULL OR c.createdAt >= ?3) AND (?4 IS NULL OR c.createdAt <= ?4) " +
            "AND (?5 IS NULL OR c.width >= ?5) AND (?6 IS NULL OR c.width <= ?6) " +
            "AND (?7 IS NULL OR c.height >= ?7) AND (?8 IS NULL OR c.height <= ?8)")
    List<Rectangle> getFilteredRectangles(String type, String createdBy, LocalDateTime createdAtFrom,
                                          LocalDateTime createdAtTo, Double widthFrom, Double widthTo,
                                          Double heightFrom, Double heightTo);
}
