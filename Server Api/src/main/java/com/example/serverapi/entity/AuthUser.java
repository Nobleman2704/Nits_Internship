package com.example.serverapi.entity;

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
        return roleList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();
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
