package com.example.serverapi.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResProductDto {
    private Long id;
    private String name;
    private Double price;
    private Integer amount;
    private LocalDateTime created;
    private String createdBy;
    private LocalDateTime updated;
    private String updatedBy;
}
