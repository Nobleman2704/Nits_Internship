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
@Entity(name = "PRODUCT")
@EntityListeners(AuditingEntityListener.class)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_id_gen")
    @SequenceGenerator(name = "PRODUCT_id_gen", sequenceName = "product_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "AMOUNT")
    private Integer amount;
    @CreatedDate
    @Column(name = "CREATED")
    private LocalDateTime created;
    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;
    @LastModifiedDate
    @Column(name = "UPDATED")
    private LocalDateTime updated;
    @LastModifiedBy
    @Column(name = "UPDATED_BY")
    private String updatedBy;
}
