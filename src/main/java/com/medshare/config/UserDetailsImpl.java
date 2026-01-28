package com.medshare.config;

import com.medshare.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {
    private UUID userId;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UUID userId, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getUserType().toString().toUpperCase());

        return new UserDetailsImpl(
                user.getUserId(),
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(authority));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public UUID getId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserDetailsImpl user = (UserDetailsImpl) o;
        return userId.equals(user.userId);
    }
}