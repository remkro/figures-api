package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Square;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface SquareRepository extends JpaRepository<Square, Long> {
    @Query("SELECT s FROM Square s WHERE (?1 IS NULL OR s.type = ?1) AND (?2 IS NULL OR s.createdBy = ?2)" +
            "AND (?3 IS NULL OR s.createdAt >= ?3) AND (?4 IS NULL OR s.createdAt <= ?4) " +
            "AND (?5 IS NULL OR s.height >= ?5) AND (?6 IS NULL OR s.height <= ?6)" +
            "AND (?7 IS NULL OR s.area >= ?7) AND (?8 IS NULL OR s.area <= ?8)" +
            "AND (?9 IS NULL OR s.perimeter >= ?9) AND (?10 IS NULL OR s.perimeter <= ?10)")
    List<Square> getFilteredSquares(String type, String createdBy, LocalDateTime createdAtFrom,
                                    LocalDateTime createdAtTo, Double heightFrom, Double heightTo, Double areaFrom,
                                    Double areaTo, Double perimeterFrom, Double perimeterTo);
}
