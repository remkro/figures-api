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
@DiscriminatorValue("SQUARE")
public class Square extends Shape {
    private double height;
    @Formula("height * height")
    private double area;
    @Formula("4 * height")
    private double perimeter;
}
