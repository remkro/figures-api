package com.geofigeo.figuresapi.service.handler;

import com.geofigeo.figuresapi.abstraction.ChangeManager;
import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.RectangleDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.entity.Rectangle;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.exception.ShapeNotFoundException;
import com.geofigeo.figuresapi.repository.RectangleRepository;
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
public class RectangleHandler implements ShapeHandler {
    private final UserRepository userRepository;
    private final RectangleRepository rectangleRepository;
    private final ModelMapper modelMapper;
    private final ChangeManager changeManager;

    @Override
    public String getShapeName() {
        return "RECTANGLE";
    }

    @Override
    public Map<String, Integer> getParamsNames() {
        Map<String, Integer> map = new HashMap<>();
        map.put("width", 0);
        map.put("height", 1);
        return map;
    }

    @Transactional
    @Override
    public ShapeDto save(AddShapeRequestDto request, String username) {
        Rectangle rectangle = createRectangle(request, username);
        Rectangle savedRectangle = rectangleRepository.saveAndFlush(rectangle);
        RectangleDto rectangleDto = mapToDto(savedRectangle);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.addShape(savedRectangle);

        return rectangleDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ShapeDto getSingle(long id) {
        Rectangle rectangle = rectangleRepository.findById(id)
                .orElseThrow(() -> new ShapeNotFoundException("Shape not found!"));
        return mapToDto(rectangle);
    }


    @Override
    public ShapeDto edit(EditShapeRequestDto request, String username) {
        Rectangle rectangle = rectangleRepository.findById(request.getId())
                .orElseThrow(() -> new ShapeNotFoundException("Shape not found!"));
        double oldWidth = rectangle.getWidth();
        double oldHeight = rectangle.getHeight();
        rectangle.setWidth(request.getParams().get(0));
        rectangle.setHeight(request.getParams().get(1));
        rectangle.setLastModifiedAt(LocalDateTime.now());
        rectangle.setLastModifiedBy(username);
        Rectangle editedRectangle = rectangleRepository.saveAndFlush(rectangle);

        Map<String, Double> oldProperties = new HashMap<>();
        oldProperties.put("oldWidth", oldWidth);
        oldProperties.put("newWidth", editedRectangle.getWidth());
        oldProperties.put("oldHeight", oldHeight);
        oldProperties.put("newHeight", editedRectangle.getHeight());
        changeManager.save(editedRectangle, username, oldProperties);

        return mapToDto(editedRectangle);
    }

    @Override
    public List<ShapeChangeDto> getChanges(long id) {
        return changeManager.getChanges(id);
    }

    private Rectangle createRectangle(AddShapeRequestDto request, String username) {
        Rectangle rectangle = new Rectangle();
        rectangle.setType(getShapeName());
        rectangle.setCreatedBy(username);
        rectangle.setCreatedAt(LocalDateTime.now());
        rectangle.setLastModifiedAt(LocalDateTime.now());
        rectangle.setLastModifiedBy(username);
        rectangle.setWidth(request.getParams().get(0));
        rectangle.setHeight(request.getParams().get(1));
        return rectangle;
    }

    private RectangleDto mapToDto(Rectangle rectangle) {
        RectangleDto rectangleDto = modelMapper.map(rectangle, RectangleDto.class);
        rectangleDto.setArea(calculateArea(rectangle.getWidth(), rectangle.getHeight()));
        rectangleDto.setPerimeter(calculatePerimeter(rectangle.getWidth(), rectangle.getHeight()));
        return rectangleDto;
    }

    private double calculateArea(double width, double height) {
        return width * height;
    }

    private double calculatePerimeter(double width, double height) {
        return 2 * (width + height);
    }
}
