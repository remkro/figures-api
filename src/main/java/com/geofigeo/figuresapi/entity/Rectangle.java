package com.geofigeo.figuresapi.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("RECTANGLE")
public class Rectangle extends Shape {
    double width;
    double height;
}
