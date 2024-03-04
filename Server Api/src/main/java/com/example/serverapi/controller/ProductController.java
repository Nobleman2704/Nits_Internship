package com.example.serverapi.controller;

import com.example.serverapi.dto.product.ReqProductDto;
import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.dto.product.ResponseDto;
import com.example.serverapi.service.ProductService;
import com.example.serverapi.util.BindingResultUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PRODUCT-CRUD")
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@EnableMethodSecurity
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER') or hasAuthority('READ')")
    @GetMapping("/get-all")
    public ResponseDto<List<ResProductDto>> getAll(Pageable pageable) {
        return ResponseDto.ok(productService.getAll(pageable));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER') or hasAuthority('READ')")
    @GetMapping("/get-by-id/{id}")
    public ResponseDto<ResProductDto> getById(@PathVariable("id") Long productId) {
        return ResponseDto.ok(productService.getById(productId));
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATE')")
    @PostMapping("/create")
    public ResponseDto<Long> create(@Valid @RequestBody ReqProductDto reqProductDto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new RuntimeException(BindingResultUtil.extractMessages(bindingResult));

        return ResponseDto.ok(productService.create(reqProductDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('UPDATE')")
    @PutMapping("/update/{id}")
    public ResponseDto<Long> update(@PathVariable("id") Long productId,
                                    @Valid @RequestBody ReqProductDto reqProductDto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new RuntimeException(BindingResultUtil.extractMessages(bindingResult));

        return ResponseDto.ok(productService.update(productId, reqProductDto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('DELETE')")
    @DeleteMapping("/delete/{id}")
    public ResponseDto<Boolean> delete(@PathVariable("id") Long productId) {
        return ResponseDto.ok(productService.delete(productId));
    }

}
