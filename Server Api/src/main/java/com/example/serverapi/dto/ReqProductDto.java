package com.example.serverapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReqProductDto {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Min(1)
    private Double price;
    @NotNull
    @Min(1)
    private Integer amount;
}
