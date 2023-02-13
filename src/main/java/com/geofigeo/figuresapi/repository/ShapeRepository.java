package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Shape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ShapeRepository extends JpaRepository<Shape, Long>, PagingAndSortingRepository<Shape, Long>, JpaSpecificationExecutor<Shape> {
    @Query("SELECT s FROM Shape s where (?1 is null or s.type = upper(?1)) and (?2 is null or s.area >= ?2) " +
            "and (?3 is null or s.area <= ?3) and (?4 is null or s.perimeter >= ?4) and (?5 is null or s.perimeter >= ?5)")
    List<Shape> getFiltered(String type, Double areaFrom, Double areaTo, Double perimeterFrom, Double perimeterTo);
}
