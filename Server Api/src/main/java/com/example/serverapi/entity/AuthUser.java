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
@Entity(name = "AUTH_USER")
@EntityListeners(AuditingEntityListener.class)
public class AuthUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUTH_USER_id_gen")
    @SequenceGenerator(name = "AUTH_USER_id_gen", sequenceName = "auth_user_seq", allocationSize = 1)
    @Column(name = "ID")
    private Long id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private List<Role> roleList;
    @CreatedDate
    @Column(name = "CREATED")
    private LocalDateTime created;
    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;
    @LastModifiedDate
    @Column(name = "UPDATED")
    private LocalDateTime updated;
    @LastModifiedBy
    @Column(name = "UPDATED_BY")
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
