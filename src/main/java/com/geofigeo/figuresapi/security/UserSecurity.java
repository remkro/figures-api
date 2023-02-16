package com.geofigeo.figuresapi.security;

import com.geofigeo.figuresapi.repository.ShapeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {
    private final ShapeRepository shapeRepository;

    public boolean isResourceCreator(Long shapeId) {
        Optional<String> createdByOptional = shapeRepository.getCreatedByById(shapeId);
        if (createdByOptional.isEmpty()) {
            return false;
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getName().equalsIgnoreCase(createdByOptional.get());
        }
    }
}
