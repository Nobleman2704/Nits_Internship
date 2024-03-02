package com.example.serverapi.controller;

import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.service.PdfService;
import com.example.serverapi.service.ProductService;
import com.example.serverapi.util.HttpUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "PDF-API")
@RestController
@RequestMapping("/api/pdf")
@RequiredArgsConstructor
public class PdfController {
    private final ProductService productService;
    private final PdfService pdfService;

    @GetMapping("download-products")
    public ResponseEntity<byte[]> downloadProducts() {
        List<ResProductDto> dtoList = productService.getAll(Pageable.ofSize(10000));

        byte[] response = pdfService.downloadProducts(dtoList);

        HttpHeaders headers = HttpUtils.getPdfHttpHeaders("products");

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}
