package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.entity.Change;
import com.geofigeo.figuresapi.entity.Role;
import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.abstraction.ChangeManager;
import com.geofigeo.figuresapi.abstraction.ShapeHandler;
import com.geofigeo.figuresapi.repository.ChangeRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void save(Shape editedShape, String username, List<Double> oldParams, ShapeHandler handler) {
        Change change = new Change();
        change.setShapeId(editedShape.getId());
        change.setLastModifiedBy(editedShape.getLastModifiedBy());
        change.setLastModifiedAt(editedShape.getLastModifiedAt());
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        Set<Role> roles = user.getRoles();
        change.setAuthor(roles.stream().map(Role::getName).toList());

        for (Map.Entry<String, Integer> entry : handler.getParamsNames().entrySet()) {
            change.addChangedValues("old" + entry.getKey(), oldParams.get(entry.getValue()));
            change.addChangedValues("new" + entry.getKey(), editedShape.getParams().get(entry.getValue()));
        }

        changeRepository.saveAndFlush(change);
    }
}
