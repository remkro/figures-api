package com.geofigeo.figuresapi.service.util;

import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.entity.Shape;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ComplexSearchPerformer {
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Shape> search(Map<String, String> searchParams, List<ShapeHandler> handlers) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Shape> cq = cb.createQuery(Shape.class);
        Root<Shape> shape = cq.from(Shape.class);
        List<Predicate> predicates = new ArrayList<>();

        if (searchParams.get("type") != null) {
            predicates.add(cb.equal(shape.get("type"), searchParams.get("type").toUpperCase()));
        }

        if (searchParams.get("createdBy") != null) {
            predicates.add(cb.equal(shape.get("createdBy"), searchParams.get("createdBy")));
        }

        if (searchParams.get("createdAfter") != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy kk:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(searchParams.get("createdAfter"), formatter);
            predicates.add(cb.greaterThanOrEqualTo(shape.get("createdAt"), dateTime));
        }

        if (searchParams.get("createdBefore") != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy kk:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(searchParams.get("createdBefore"), formatter);
            predicates.add(cb.lessThanOrEqualTo(shape.get("createdAt"), dateTime));
        }

        if (searchParams.get("areaFrom") != null) {
            predicates.add(cb.greaterThanOrEqualTo(shape.get("area"), searchParams.get("areaFrom")));
        }

        if (searchParams.get("areaTo") != null) {
            predicates.add(cb.lessThanOrEqualTo(shape.get("area"), searchParams.get("areaTo")));
        }

        if (searchParams.get("perimeterFrom") != null) {
            predicates.add(cb.greaterThanOrEqualTo(shape.get("perimeter"), searchParams.get("perimeterFrom")));
        }

        if (searchParams.get("perimeterTo") != null) {
            predicates.add(cb.lessThanOrEqualTo(shape.get("perimeter"), searchParams.get("perimeterTo")));
        }

        handlers.forEach(handler -> {
            for (Map.Entry<String, Integer> entry : handler.getParamsNames().entrySet()) {
                if (searchParams.get(entry.getKey() + "From") != null) {
                    MapJoin<Object, Object, Object> map = shape.joinMap("properties");
                    predicates.add(cb.and(cb.equal(map.key(), entry.getKey()),
                            cb.greaterThanOrEqualTo(map.value().as(Double.class),
                                    Double.valueOf(searchParams.get(entry.getKey() + "From")))));
                }
            }
        });

        handlers.forEach(handler -> {
            for (Map.Entry<String, Integer> entry : handler.getParamsNames().entrySet()) {
                if (searchParams.get(entry.getKey() + "To") != null) {
                    MapJoin<Object, Object, Object> map = shape.joinMap("properties");
                    predicates.add(cb.and(cb.equal(map.key(), entry.getKey()),
                            cb.lessThanOrEqualTo(map.value().as(Double.class),
                                    Double.valueOf(searchParams.get(entry.getKey() + "To")))));
                }
            }
        });

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}
