package com.example.serverapi.service;

import com.example.serverapi.dto.product.ReqProductDto;
import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.entity.Product;
import com.example.serverapi.mapper.ProductMapper;
import com.example.serverapi.repository.ProductRepository;
import com.example.serverapi.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
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

        when(productMapper.toEntity(eq(productDto))).thenReturn(product);

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

        List<ResProductDto> resDtoList = List.of(resDto, resDto1);

        when(productService.getAll(Pageable.ofSize(10))).thenReturn(resDtoList);
    }

    @Test
    void update() {
        ReqProductDto reqDto = new ReqProductDto();
        reqDto.setName("Apple");
        reqDto.setAmount(74);
        reqDto.setPrice(10.5D);

        ReqProductDto reqDto1 = new ReqProductDto();
        reqDto1.setName("banana");
        reqDto1.setAmount(74);
        reqDto1.setPrice(10.5D);

        ResProductDto resDto = new ResProductDto();
        resDto.setName("banana");
        resDto.setAmount(74);
        resDto.setPrice(10.5D);

        Long id = productService.create(reqDto);

        when(productService.update(id, reqDto1)).thenReturn(id);
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

        when(productService.getById(id)).thenReturn(resDto);
    }

    @Test
    void delete() {
        ReqProductDto reqDto = new ReqProductDto();
        reqDto.setName("Apple");
        reqDto.setAmount(74);
        reqDto.setPrice(10.5D);

        ResProductDto resDto = new ResProductDto();
        resDto.setName("Apple");
        resDto.setAmount(74);
        resDto.setPrice(10.5D);

        Long id = productService.create(reqDto);

        when(productService.delete(id)).thenReturn(true);
    }
}