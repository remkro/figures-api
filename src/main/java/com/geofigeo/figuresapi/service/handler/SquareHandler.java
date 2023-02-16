package com.geofigeo.figuresapi.service.handler;

import com.geofigeo.figuresapi.abstraction.ChangeManager;
import com.geofigeo.figuresapi.dto.AddShapeRequestDto;
import com.geofigeo.figuresapi.dto.EditShapeRequestDto;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.dto.ShapeDto;
import com.geofigeo.figuresapi.dto.SquareDto;
import com.geofigeo.figuresapi.entity.Square;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.exception.ShapeNotFoundException;
import com.geofigeo.figuresapi.repository.SquareRepository;
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
public class SquareHandler implements ShapeHandler {
    private final UserRepository userRepository;
    private final SquareRepository squareRepository;
    private final ModelMapper modelMapper;
    private final ChangeManager changeManager;

    @Override
    public String getShapeName() {
        return "SQUARE";
    }

    @Override
    public Map<String, Integer> getParamsNames() {
        Map<String, Integer> map = new HashMap<>();
        map.put("height", 0);
        return map;
    }

    @Transactional
    @Override
    public ShapeDto save(AddShapeRequestDto request, String username) {
        Square square = createSquare(request, username);
        Square savedSquare = squareRepository.saveAndFlush(square);
        SquareDto squareDto = mapToDto(savedSquare);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        user.addShape(savedSquare);

        return squareDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ShapeDto getSingle(long id) {
        Square square = squareRepository.findById(id)
                .orElseThrow(() -> new ShapeNotFoundException("Shape not found!"));
        return mapToDto(square);
    }

    @Override
    public ShapeDto edit(EditShapeRequestDto request, String username) {
        Square square = squareRepository.findById(request.getId())
                .orElseThrow(() -> new ShapeNotFoundException("Shape not found!"));
        double oldHeight = square.getHeight();
        square.setHeight(request.getParams().get(0));
        square.setLastModifiedAt(LocalDateTime.now());
        square.setLastModifiedBy(username);
        Square editedSquare = squareRepository.saveAndFlush(square);

        Map<String, Double> oldProperties = new HashMap<>();
        oldProperties.put("oldHeight", oldHeight);
        oldProperties.put("newHeight", editedSquare.getHeight());
        changeManager.save(editedSquare, username, oldProperties);

        return mapToDto(editedSquare);
    }

    @Override
    public List<ShapeChangeDto> getChanges(long id) {
        return changeManager.getChanges(id);
    }

    private Square createSquare(AddShapeRequestDto request, String username) {
        Square square = new Square();
        square.setType(getShapeName());
        square.setCreatedBy(username);
        square.setCreatedAt(LocalDateTime.now());
        square.setLastModifiedAt(LocalDateTime.now());
        square.setLastModifiedBy(username);
        square.setHeight(request.getParams().get(0));
        return square;
    }

    private SquareDto mapToDto(Square square) {
        SquareDto squareDto = modelMapper.map(square, SquareDto.class);
        squareDto.setArea(calculateArea(square.getHeight()));
        squareDto.setPerimeter(calculatePerimeter(square.getHeight()));
        return squareDto;
    }

    private double calculateArea(double height) {
        return height * height;
    }

    private double calculatePerimeter(double height) {
        return 4 * height;
    }
}
