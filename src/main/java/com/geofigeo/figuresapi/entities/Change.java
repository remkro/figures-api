package com.geofigeo.figuresapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Change {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long shapeId;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    @ElementCollection
    private List<String> author;
    @ElementCollection
    private Map<String, Double> changedValues = new HashMap<>();

    public void addChangedValues(String name, Double value) {
        changedValues.put(name, value);
    }
}
