package com.example.serverapi.service.impl;

import com.example.serverapi.dto.product.ReqProductDto;
import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.entity.Product;
import com.example.serverapi.mapper.ProductMapper;
import com.example.serverapi.repository.ProductRepository;
import com.example.serverapi.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ResProductDto> getAll(Pageable pageable) {
        return productMapper.toDto(productRepository.findAll());
    }

    @Override
    public Long create(ReqProductDto reqProductDto) {
        Product product = productMapper.toEntity(reqProductDto);
        return productRepository.save(product).getId();
    }

    @Override
    public Long update(Long productId, ReqProductDto reqProductDto) {
        Product product = findProductById(productId);

        productMapper.mergeFromDto(reqProductDto, product);

        return productRepository.save(product).getId();
    }

    @Override
    public ResProductDto getById(Long productId) {
        return productMapper.toDto(findProductById(productId));
    }

    @Override
    public Boolean delete(Long productId) {
        findProductById(productId);

        productRepository.deleteById(productId);
        return true;
    }


    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Entity not found by this id: %s", productId)));
    }
}
