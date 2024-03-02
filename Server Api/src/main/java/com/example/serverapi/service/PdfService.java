package com.example.serverapi.service;

import com.example.serverapi.dto.product.ResProductDto;

import java.util.List;

public interface PdfService {
    byte[] downloadProducts(List<ResProductDto> dtoList);
}
