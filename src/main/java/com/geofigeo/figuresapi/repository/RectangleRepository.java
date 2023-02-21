package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Rectangle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RectangleRepository extends JpaRepository<Rectangle, Long> {
    @Query("SELECT r FROM Rectangle r WHERE (?1 IS NULL OR r.type = ?1) AND (?2 IS NULL OR r.createdBy = ?2)" +
            "AND (?3 IS NULL OR r.createdAt >= ?3) AND (?4 IS NULL OR r.createdAt <= ?4) " +
            "AND (?5 IS NULL OR r.width >= ?5) AND (?6 IS NULL OR r.width <= ?6) " +
            "AND (?7 IS NULL OR r.height >= ?7) AND (?8 IS NULL OR r.height <= ?8)" +
            "AND (?9 IS NULL OR r.area >= ?9) AND (?10 IS NULL OR r.area <= ?10)" +
            "AND (?11 IS NULL OR r.perimeter >= ?11) AND (?12 IS NULL OR r.perimeter <= ?12)")
    List<Rectangle> getFilteredRectangles(String type, String createdBy, LocalDateTime createdAtFrom,
                                          LocalDateTime createdAtTo, Double widthFrom, Double widthTo,
                                          Double heightFrom, Double heightTo, Double areaFrom, Double areaTo,
                                          Double perimeterFrom, Double perimeterTo);
}
