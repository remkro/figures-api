package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Circle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CircleRepository extends JpaRepository<Circle, Long> {
    @Query("SELECT c FROM Circle c WHERE (?1 IS NULL OR c.type = ?1) AND (?2 IS NULL OR c.createdBy = ?2)" +
            "AND (?3 IS NULL OR c.createdAt >= ?3) AND (?4 IS NULL OR c.createdAt <= ?4) " +
            "AND (?5 IS NULL OR c.radius >= ?5) AND (?6 IS NULL OR c.radius <= ?6)")
    List<Circle> getFilteredCircles(String type, String createdBy, LocalDateTime createdAtFrom,
                                    LocalDateTime createdAtTo, Double radiusFrom, Double radiusTo);
}
