package com.geofigeo.figuresapi.service.handler;

import com.geofigeo.figuresapi.abstraction.ChangeManager;
import com.geofigeo.figuresapi.configuration.AppConstants;
import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.CircleDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Circle;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.exception.ShapeNotFoundException;
import com.geofigeo.figuresapi.repository.CircleRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CircleHandler implements ShapeHandler {
    private final UserRepository userRepository;
    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper;
    private final ChangeManager changeManager;

    @Override
    public String getShapeName() {
        return "CIRCLE";
    }

    @Override
    public Map<String, Integer> getParamsNames() {
        Map<String, Integer> map = new HashMap<>();
        map.put("radius", 0);
        return map;
    }

    @Transactional
    @Override
    public ShapeDto save(AddShapeRequestDto request, String username) {
        Circle circle = createCircle(request, username);
        Circle savedCircle = circleRepository.saveAndFlush(circle);
        CircleDto circleDto = mapToDto(savedCircle);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.addShape(savedCircle);

        return circleDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ShapeDto getSingle(long id) {
        Circle circle = circleRepository.findById(id)
                .orElseThrow(() -> new ShapeNotFoundException("Shape not found!"));
        return mapToDto(circle);
    }

    @Transactional
    @Override
    public ShapeDto edit(EditShapeRequestDto request, String username) {
        Circle circle = circleRepository.findById(request.getId())
                .orElseThrow(() -> new ShapeNotFoundException("Shape not found!"));
        double oldRadius = circle.getRadius();
        circle.setRadius(request.getParams().get(0));
        circle.setLastModifiedAt(LocalDateTime.now());
        circle.setLastModifiedBy(username);
        Circle editedCircle = circleRepository.saveAndFlush(circle);

        Map<String, Double> oldProperties = new HashMap<>();
        oldProperties.put("oldRadius", oldRadius);
        oldProperties.put("newRadius", editedCircle.getRadius());
        changeManager.save(editedCircle, username, oldProperties);

        return mapToDto(editedCircle);
    }

    @Override
    public List<ShapeChangeDto> getChanges(long id) {
        return changeManager.getChanges(id);
    }

    @Override
    public List<ShapeDto> getFiltered(Map<String, String> searchParams) {
        String type = searchParams.get("type") == null ? null : searchParams.get("type").toUpperCase();
        String createdBy = searchParams.get("createdBy");
        LocalDateTime createdAtFrom = searchParams.get("createdAtFrom") == null ? null :
                LocalDateTime.parse(searchParams.get("createdAtFrom"), AppConstants.formatter);
        LocalDateTime createdAtTo = searchParams.get("createdAtTo") == null ? null :
                LocalDateTime.parse(searchParams.get("createdAtTo"), AppConstants.formatter);
        Double radiusFrom = searchParams.get("radiusFrom") == null ? null :
                Double.parseDouble(searchParams.get("radiusFrom"));
        Double radiusTo = searchParams.get("radiusTo") == null ? null :
                Double.parseDouble(searchParams.get("radiusTo"));

        List<CircleDto> circles = circleRepository.getFilteredCircles(type, createdBy, createdAtFrom, createdAtTo,
                radiusFrom, radiusTo).stream().map(this::mapToDto).toList();
        return List.copyOf(circles);
    }

    private Circle createCircle(AddShapeRequestDto request, String username) {
        Circle circle = new Circle();
        circle.setType(getShapeName());
        circle.setCreatedBy(username);
        circle.setCreatedAt(LocalDateTime.now());
        circle.setLastModifiedAt(LocalDateTime.now());
        circle.setLastModifiedBy(username);
        circle.setRadius(request.getParams().get(0));
        return circle;
    }

    private CircleDto mapToDto(Circle circle) {
        CircleDto circleDto = modelMapper.map(circle, CircleDto.class);
        circleDto.setArea(calculateArea(circle.getRadius()));
        circleDto.setPerimeter(calculatePerimeter(circle.getRadius()));
        return circleDto;
    }

    private double calculateArea(double radius) {
        return Math.PI * (Math.pow(radius, 2));
    }

    private double calculatePerimeter(double radius) {
        return 2 * Math.PI * radius;
    }
}
