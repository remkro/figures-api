package com.geofigeo.figuresapi.repository;

import com.geofigeo.figuresapi.entity.Shape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ShapeRepository extends JpaRepository<Shape, Long>, PagingAndSortingRepository<Shape, Long>,
        JpaSpecificationExecutor<Shape> {
}
