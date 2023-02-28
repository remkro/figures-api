package com.geofigeo.figuresapi.service;

import com.geofigeo.figuresapi.dto.ShapeChangeDto;
import com.geofigeo.figuresapi.entity.Change;
import com.geofigeo.figuresapi.entity.Role;
import com.geofigeo.figuresapi.entity.Shape;
import com.geofigeo.figuresapi.entity.User;
import com.geofigeo.figuresapi.repository.ChangeRepository;
import com.geofigeo.figuresapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChangeManagerImplTest {
    @InjectMocks
    private ChangeManagerImpl changeManager;
    @Mock
    private ChangeRepository changeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void should_save_change() {
        // Create shape
        Shape editedShape = new Shape();
        editedShape.setId(1L);
        editedShape.setLastModifiedBy("user1");
        editedShape.setLastModifiedAt(LocalDateTime.now());

        // Create old properties
        Map<String, Double> oldProperties = new HashMap<>();
        oldProperties.put("length", 10.0);
        oldProperties.put("width", 5.0);

        // Create user
        User user = new User();
        user.setUsername("user1");
        Role role = new Role();
        role.setName("CREATOR");
        user.setRoles(Set.of(role));

        // Mock the userRepository to return the test user
        Mockito.when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        // Call the save() method
        changeManager.save(editedShape, "user1", oldProperties);

        // Verify that repository was invoked once
        verify(changeRepository, times(1)).saveAndFlush(any(Change.class));

        // Verify that the change was saved with the expected values
        ArgumentCaptor<Change> changeCaptor = ArgumentCaptor.forClass(Change.class);
        Mockito.verify(changeRepository).saveAndFlush(changeCaptor.capture());
        Change savedChange = changeCaptor.getValue();

        //Assert that expected and actual values are equal
        assertEquals(savedChange.getShapeId(), editedShape.getId());
        assertEquals(savedChange.getLastModifiedBy(), editedShape.getLastModifiedBy());
        assertEquals(savedChange.getLastModifiedAt(), editedShape.getLastModifiedAt());
        assertEquals(savedChange.getAuthor(), List.of("CREATOR"));
        assertEquals(savedChange.getChangedValues(), oldProperties);
    }

    @Test
    void should_get_list_of_changes() {
        // Create some example data
        long shapeId = 1L;
        List<Change> changes = new ArrayList<>();
        Change change1 = new Change();
        change1.setId(1L);
        change1.setShapeId(shapeId);
        Change change2 = new Change();
        change2.setId(2L);
        change2.setShapeId(shapeId);
        changes.add(change1);
        changes.add(change2);

        ShapeChangeDto dto1 = new ShapeChangeDto();
        ShapeChangeDto dto2 = new ShapeChangeDto();

        //Mock modelMapper to return DTO objects
        Mockito.when(modelMapper.map(change1, ShapeChangeDto.class)).thenReturn(dto1);
        Mockito.when(modelMapper.map(change2, ShapeChangeDto.class)).thenReturn(dto2);

        //Mock the changeRepository to return prepared changes
        Mockito.when(changeRepository.findAllByShapeId(shapeId)).thenReturn(changes);

        // Call the getChanges() method
        List<ShapeChangeDto> result = changeManager.getChanges(shapeId);

        //Assert that expected and actual values are equal
        assertEquals(result.size(), 2);
        assertEquals(result.get(0), dto1);
        assertEquals(result.get(1), dto2);
    }
}