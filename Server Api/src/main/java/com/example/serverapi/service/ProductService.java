package com.example.serverapi.service;

import com.example.serverapi.dto.product.ReqProductDto;
import com.example.serverapi.dto.product.ResProductDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    List<ResProductDto> getAll(Pageable pageable);

    Long create(ReqProductDto reqProductDto);

    Long update(Long productId, ReqProductDto reqProductDto);

    ResProductDto getById(Long productId);

    Boolean delete(Long productId);
}
