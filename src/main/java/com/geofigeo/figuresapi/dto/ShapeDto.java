package com.geofigeo.figuresapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShapeDto {
    private long id;
    private String type;
    private String createdBy;
    @JsonFormat(pattern="dd-MM-yyyy kk:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern="dd-MM-yyyy kk:mm:ss")
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    private double area;
    private double perimeter;
}
