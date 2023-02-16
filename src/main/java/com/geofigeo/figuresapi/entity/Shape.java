package com.geofigeo.figuresapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type",
        discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
public class Shape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(insertable = false, updatable = false)
    private String type;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    @Version
    private int version;
}
