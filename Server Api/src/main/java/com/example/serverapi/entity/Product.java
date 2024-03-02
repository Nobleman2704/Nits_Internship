package com.example.serverapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_id_gen")
    @SequenceGenerator(name = "PRODUCT_id_gen", sequenceName = "product_seq", allocationSize = 1)
    private Long id;
    private String name;
    private Double price;
    private Integer amount;
    @CreatedDate
    private LocalDateTime created;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updated;
    @LastModifiedBy
    private String updatedBy;
}
