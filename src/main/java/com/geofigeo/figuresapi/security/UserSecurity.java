package com.geofigeo.figuresapi.security;

import com.geofigeo.figuresapi.repository.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    private final ShapeRepository shapeRepository;

    public boolean isResourceCreator(Long shapeId) {
        return shapeRepository.getCreatedByById(shapeId).map(username -> SecurityContextHolder.getContext()
                .getAuthentication().getName().equalsIgnoreCase(username)).orElse(false);
    }
}
