package com.geofigeo.figuresapi.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("RECTANGLE")
public class Rectangle extends Shape {
    double width;
    double height;
    @Formula("width * height")
    private double area;
    @Formula("2 * (width + height)")
    private double perimeter;
}
