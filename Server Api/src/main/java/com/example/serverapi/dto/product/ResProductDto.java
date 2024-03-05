package com.example.serverapi.dto.product;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

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




    /*For testing*/
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ResProductDto that = (ResProductDto) object;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, amount);
    }
}
