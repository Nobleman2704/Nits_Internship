package com.example.serverapi.controller;

import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.service.ExcelService;
import com.example.serverapi.service.ProductService;
import com.example.serverapi.util.HttpUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "EXCEL-API")
@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
@EnableMethodSecurity
public class ExcelController {
    private final ProductService productService;
    private final ExcelService excelService;

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('DOWNLOAD')")
    @GetMapping("/download-products")
    public ResponseEntity<byte[]> downloadProducts() {
        List<ResProductDto> dtoList = productService.getAll(Pageable.ofSize(10000));

        byte[] response = excelService.downloadProducts(dtoList);

        HttpHeaders headers = HttpUtils.getHttpHeaders("products");

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
