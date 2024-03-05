package com.example.serverapi.service;

import com.example.serverapi.dto.product.ReqProductDto;
import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.entity.Product;
import com.example.serverapi.mapper.ProductMapper;
import com.example.serverapi.repository.ProductRepository;
import com.example.serverapi.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductServiceImpl productService;

    @Test
    void create() {
        ReqProductDto productDto = new ReqProductDto();
        productDto.setName("Apple");
        productDto.setAmount(74);
        productDto.setPrice(10.5D);

        Product product = new Product();
        product.setName("Apple");
        product.setAmount(74);
        product.setPrice(10.5D);

        Product product1 = productMapper.toEntity(productDto);

        assertThat(product1.getName()).isEqualTo(product.getName());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getName()).isEqualTo(product.getName());

        Long id = productService.create(productDto);

        assertThat(id).isNotNull();
    }

    @Test
    void getAll() {
        ReqProductDto reqDto = new ReqProductDto();
        reqDto.setName("Apple");
        reqDto.setAmount(74);
        reqDto.setPrice(10.5D);

        ReqProductDto reqDto1 = new ReqProductDto();
        reqDto1.setName("banana");
        reqDto1.setAmount(74);
        reqDto1.setPrice(1.5D);

        productService.create(reqDto);
        productService.create(reqDto1);

        ResProductDto resDto = new ResProductDto();
        resDto.setName("Apple");
        resDto.setAmount(74);
        resDto.setPrice(10.5D);

        ResProductDto resDto1 = new ResProductDto();
        resDto1.setName("banana");
        resDto1.setAmount(74);
        resDto1.setPrice(1.5D);

        List<ResProductDto> resProductDtoList = productService.getAll(Pageable.ofSize(10));

        assertThat(resProductDtoList.containsAll(List.of(resDto, resDto1))).isEqualTo(true);
    }

    @Test
    void update() {
        ReqProductDto reqDto = new ReqProductDto();
        reqDto.setName("Apple");
        reqDto.setAmount(74);
        reqDto.setPrice(10.5D);

        Long id = productService.create(reqDto);

        ReqProductDto reqDto1 = new ReqProductDto();
        reqDto1.setName("banana");
        reqDto1.setAmount(7);
        reqDto1.setPrice(46.69D);

        productService.update(id, reqDto1);

        ResProductDto resDto = new ResProductDto();
        resDto.setName("banana");
        resDto.setAmount(7);
        resDto.setPrice(46.69D);

        assertThat(productService.getById(id)).isEqualTo(resDto);
    }

    @Test
    void getById() {
        ReqProductDto reqDto = new ReqProductDto();
        reqDto.setName("Apple");
        reqDto.setAmount(74);
        reqDto.setPrice(10.5D);

        ResProductDto resDto = new ResProductDto();
        resDto.setName("Apple");
        resDto.setAmount(74);
        resDto.setPrice(10.5D);

        Long id = productService.create(reqDto);

        assertThat(productService.getById(id)).isEqualTo(resDto);
    }

    @Test
    void delete() {
        ReqProductDto reqDto = new ReqProductDto();
        reqDto.setName("Apple");
        reqDto.setAmount(74);
        reqDto.setPrice(10.5D);

        Long id = productService.create(reqDto);

        assertThat(productService.delete(id)).isEqualTo(true);
    }
}