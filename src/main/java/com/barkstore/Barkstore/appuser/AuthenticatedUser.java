package com.barkstore.Barkstore.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AuthenticatedUser implements UserDetails {
    @Autowired
    private MyUser user;
    private List<GrantedAuthority> authorities;

    public AuthenticatedUser(MyUser user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getMiddleName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getFirstName();
    }

    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }

    public String getRole() {
        Set<Role> role = user.getRoles();
        String userRole = "";
        for (Role i : role) {
            userRole = i.getName();
        }

        return userRole;
    }




}
