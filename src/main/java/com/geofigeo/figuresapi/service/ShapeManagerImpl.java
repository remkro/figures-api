package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Change;
import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.exception.ShapeNotSupportedException;
import com.geofigeo.figuresapi.abstraction.ChangeManager;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.abstraction.ShapeManager;
import com.geofigeo.figuresapi.repository.ChangeRepository;
import com.geofigeo.figuresapi.repository.ShapeRepository;
import com.geofigeo.figuresapi.service.util.ComplexSearchPerformer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.MapJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShapeManagerImpl implements ShapeManager {
    private final ShapeRepository shapeRepository;
    private final ChangeRepository changeRepository;
    private final List<ShapeHandler> handlers;
    private final ModelMapper modelMapper;
    private final ChangeManager changeManager;
    private final ComplexSearchPerformer performer;
    private final EntityManager entityManager;
    private static final Logger logger = LoggerFactory.getLogger(ShapeManagerImpl.class);

    @Override
    public ShapeDto save(AddShapeRequestDto addShapeRequestDto, String username) {
        ShapeHandler handler = getHandler(addShapeRequestDto.getType());
        Shape partialShape = createPartialShape(username);
        logger.info("Adding shape: " + addShapeRequestDto.getType());
        return handler.save(partialShape, addShapeRequestDto, username);
    }

    @Transactional(readOnly = true)
    @Override
    public ShapeDto getSingleShape(long id) {
        if (!shapeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shape not found!");
        }
        Shape shape = shapeRepository.findById(id).get();
        ShapeHandler handler = getHandler(shape.getType());
        return handler.mapShapeToSpecificDto(shape);
    }

    @Transactional
    @Override
    public ShapeDto editSingleShape(EditShapeRequestDto editShapeDto, String username) {
        if (!shapeRepository.existsById(editShapeDto.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shape not found!");
        }

        Shape shape = shapeRepository.findById(editShapeDto.getId()).get();
        ShapeHandler handler = getHandler(shape.getType());
        Map<String, Double> oldProperties = new HashMap<>(shape.getProperties());
        for (Map.Entry<String, Integer> entry : handler.getParamsNames().entrySet()) {
            shape.addProperty(entry.getKey(), editShapeDto.getParams().get(entry.getValue()));
        }
        shape.setLastModifiedBy(username);
        shape.setLastModifiedAt(LocalDateTime.now());
        shape = handler.edit(shape);

        Shape editedShape = shapeRepository.saveAndFlush(shape);
        changeManager.save(editedShape, username, oldProperties, handler);

        return handler.mapShapeToSpecificDto(editedShape);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ShapeChangeDto> getShapeChanges(long id) {
        List<Change> changes = changeRepository.getAllByShapeId(id);
        return changes.stream().map(c -> modelMapper.map(c, ShapeChangeDto.class)).toList();
    }

    public List<ShapeDto> getFilteredShapes(Map<String, String> searchParams) {
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Shape> cq = cb.createQuery(Shape.class);
//        Root<Shape> shape = cq.from(Shape.class);
//        List<Predicate> predicates = new ArrayList<>();
//
//        if (searchParams.get("type") != null) {
//            predicates.add(cb.equal(shape.get("type"), searchParams.get("type").toUpperCase()));
//        }
//
//        if (searchParams.get("createdBy") != null) {
//            predicates.add(cb.equal(shape.get("createdBy"), searchParams.get("createdBy")));
//        }
//
//        if (searchParams.get("createdAfter") != null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy kk:mm:ss");
//            LocalDateTime dateTime = LocalDateTime.parse(searchParams.get("createdAfter"), formatter);
//            predicates.add(cb.greaterThanOrEqualTo(shape.get("createdAt"), dateTime));
//        }
//
//        if (searchParams.get("createdBefore") != null) {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy kk:mm:ss");
//            LocalDateTime dateTime = LocalDateTime.parse(searchParams.get("createdBefore"), formatter);
//            predicates.add(cb.lessThanOrEqualTo(shape.get("createdAt"), dateTime));
//        }
//
//        if (searchParams.get("areaFrom") != null) {
//            predicates.add(cb.greaterThanOrEqualTo(shape.get("area"), searchParams.get("areaFrom")));
//        }
//
//        if (searchParams.get("areaTo") != null) {
//            predicates.add(cb.lessThanOrEqualTo(shape.get("area"), searchParams.get("areaTo")));
//        }
//
//        if (searchParams.get("perimeterFrom") != null) {
//            predicates.add(cb.greaterThanOrEqualTo(shape.get("perimeter"), searchParams.get("perimeterFrom")));
//        }
//
//        if (searchParams.get("perimeterTo") != null) {
//            predicates.add(cb.lessThanOrEqualTo(shape.get("perimeter"), searchParams.get("perimeterTo")));
//        }
//
//        handlers.forEach(handler -> {
//            for (Map.Entry<String, Integer> entry : handler.getParamsNames().entrySet()) {
//                if (searchParams.get(entry.getKey() + "From") != null) {
//                    MapJoin<Object, Object, Object> map = shape.joinMap("properties");
//                    predicates.add(cb.and(cb.equal(map.key(), entry.getKey()),
//                            cb.greaterThanOrEqualTo(map.value().as(Double.class),
//                                    Double.valueOf(searchParams.get(entry.getKey() + "From")))));
//                }
//            }
//        });
//
//        handlers.forEach(handler -> {
//            for (Map.Entry<String, Integer> entry : handler.getParamsNames().entrySet()) {
//                if (searchParams.get(entry.getKey() + "To") != null) {
//                    MapJoin<Object, Object, Object> map = shape.joinMap("properties");
//                    predicates.add(cb.and(cb.equal(map.key(), entry.getKey()),
//                            cb.lessThanOrEqualTo(map.value().as(Double.class),
//                                    Double.valueOf(searchParams.get(entry.getKey() + "To")))));
//                }
//            }
//        });
//
//        cq.where(predicates.toArray(new Predicate[0]));
//        entityManager.createQuery(cq).getResultList();
        List<Shape> result = performer.search(searchParams, handlers);
        return result.stream().map(s -> modelMapper.map(s, ShapeDto.class)).toList();
    }

    @Transactional(readOnly = true)
    public List<Shape> getAll() {
        return shapeRepository.findAll();
    }

    private ShapeHandler getHandler(String shapeName) {
        return handlers.stream().filter(h -> h.getShapeName().equalsIgnoreCase(shapeName)).findFirst()
                .orElseThrow(() -> new ShapeNotSupportedException("Shape not supported! Handler not found!"));
    }

    private Shape createPartialShape(String username) {
        Shape shape = new Shape();
        shape.setCreatedBy(username);
        shape.setCreatedAt(LocalDateTime.now());
        shape.setLastModifiedAt(LocalDateTime.now());
        shape.setLastModifiedBy(username);
        return shape;
    }
}
