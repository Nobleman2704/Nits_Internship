package com.example.serverapi.mapper;

import com.example.serverapi.dto.product.ReqProductDto;
import com.example.serverapi.dto.product.ResProductDto;
import com.example.serverapi.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface ProductMapper {
    Product toEntity(ReqProductDto reqProductDto);

    ResProductDto toDto(Product product);

    List<ResProductDto> toDto(List<Product> productList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mergeFromDto(ReqProductDto reqProductDto, @MappingTarget Product product);
}
