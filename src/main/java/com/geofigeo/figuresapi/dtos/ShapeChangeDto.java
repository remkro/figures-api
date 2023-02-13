package com.geofigeo.figuresapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ShapeChangeDto {
    private long shapeId;
    @JsonFormat(pattern="dd-MM-yyyy kk:mm:ss")
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    private List<String> author;
    private Map<String, Double> changedValues = new HashMap<>();
}
