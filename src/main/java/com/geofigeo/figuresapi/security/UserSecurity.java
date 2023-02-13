package com.geofigeo.figuresapi.security;

import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.repository.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    private final ShapeRepository shapeRepository;

    public boolean isResourceCreator(Long shapeId) {
        boolean present = shapeRepository.findById(shapeId).isPresent();
        if(!present) {
            return false;
        }
        Shape shape = shapeRepository.findById(shapeId).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equalsIgnoreCase(shape.getCreatedBy());
    }
}
