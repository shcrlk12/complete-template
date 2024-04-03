package com.unison.scada.availability.global.config.security;

import com.unison.scada.availability.domain.user.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserDetailImpl implements UserDetails {

    private final Users users;

    public UserDetailImpl(Users users){
        this.users = users;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(users.getRole().name()));

        return authorities;
    }

    @Override
    public String getPassword() {

        return Optional.ofNullable(users).map(Users::getPw).orElse("");
    }

    @Override
    public String getUsername() {
        return Optional.ofNullable(users).map(Users::getId).orElse("");

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
