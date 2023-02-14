package com.geofigeo.figuresapi.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
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
@Table(name = "CHANGES")
public class Change {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long shapeId;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    @ElementCollection
    @CollectionTable(name = "change_authors", joinColumns = {@JoinColumn(name = "change_id")})
    @Column(name = "author")
    private List<String> author;
    @ElementCollection
    @CollectionTable(name = "change_properties", joinColumns = {@JoinColumn(name = "change_id")})
    @MapKeyColumn(name = "name")
    @Column(name = "num_value")
    private Map<String, Double> changedValues = new HashMap<>();

    public void addChangedValues(String name, Double value) {
        changedValues.put(name, value);
    }
}
