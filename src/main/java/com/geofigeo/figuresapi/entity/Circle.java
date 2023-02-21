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
@DiscriminatorValue("CIRCLE")
public class Circle extends Shape {
    private double radius;
    @Formula("3.141592 * radius * radius")
    private double area;
    @Formula("2 * 3.141592 * radius")
    private double perimeter;
}
