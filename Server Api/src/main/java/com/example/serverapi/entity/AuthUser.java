package com.example.serverapi.entity;

import com.example.serverapi.enums.Authority;
import com.example.serverapi.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity(name = "auth_user")
@EntityListeners(AuditingEntityListener.class)
public class AuthUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_USER_id_gen")
    @SequenceGenerator(name = "AUTH_USER_id_gen", sequenceName = "auth_user_seq", allocationSize = 1)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private List<Role> roleList;
    @Enumerated(EnumType.STRING)
    private List<Authority> authorityList;
    @CreatedDate
    private LocalDateTime created;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime updated;
    @LastModifiedBy
    private String updatedBy;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = roleList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.name())))
                .collect(Collectors.toList());

        if (!authorityList.isEmpty())
            authorityList.forEach(authority -> {
                list.add(new SimpleGrantedAuthority(authority.name()));
            });
        return list;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
