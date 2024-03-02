package com.example.serverapi.mapper;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.entity.AuthUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.FIELD)
public interface AuthUserMapperMapper {
    AuthUser toEntity(AuthAndRegRequest authAndRegRequest);

    AuthAndRegRequest toDto(AuthUser authUser);
}
