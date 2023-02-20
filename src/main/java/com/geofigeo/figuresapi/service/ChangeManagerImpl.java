package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.abstraction.ChangeManager;
import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.entity.Change;
import com.geofigeo.figuresapi.entity.Role;
import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.repository.ChangeRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChangeManagerImpl implements ChangeManager {
    private final ChangeRepository changeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void save(Shape editedShape, String username, Map<String, Double> oldProperties) {
        Change change = new Change();
        change.setShapeId(editedShape.getId());
        change.setLastModifiedBy(editedShape.getLastModifiedBy());
        change.setLastModifiedAt(editedShape.getLastModifiedAt());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USERNAME_NOT_FOUND"));
        Set<Role> roles = user.getRoles();
        change.setAuthor(roles.stream().map(Role::getName).toList());
        change.setChangedValues(oldProperties);
        changeRepository.saveAndFlush(change);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ShapeChangeDto> getChanges(long shapeId) {
        return changeRepository.findAllByShapeId(shapeId).stream()
                .map(s -> modelMapper.map(s, ShapeChangeDto.class)).toList();
    }
}
