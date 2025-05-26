package com.barkstore.Barkstore.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private MyUserService userService;

    public void createRole(RoleRequest roleRequest, List<String> auths) {
        Role role = new Role();
        role.setName(roleRequest.getName());

        Set<Authority> authorities = saveAuthorityToRole(auths);

        System.out.println("Auths: " + authorities);
        role.setAuthorities(authorities);
        roleRepository.save(role);
    }

    public Set<Authority> saveAuthorityToRole(List<String> auths) {
        Set<Authority> authorities = new HashSet<>();
        for (String str : auths) {
            Authority authority = authorityRepository.findByName(str).get();
            authorities.add(authority);
        }
        return authorities;
    }

    public void showSavedRoleAuth(Long id) {
        Role role = roleRepository.findById(id).get();
        Set<Authority> authorities = new HashSet<>();
        System.out.println("Saved auths: ");
        for (Authority auth : role.getAuthorities()) {
            System.out.println(auth.getName());
        }

    }

    public List<String> showAuthForUpdate(Long id) {
        List<Authority> authorities = authorityRepository.findAll();
        Role role = roleRepository.findById(id).get();
        List<String> availableAuths = new ArrayList<>();
        authorities.removeAll(role.getAuthorities());
        for (Authority authority : authorities) {
            availableAuths.add(authority.getName());
        }
//        System.out.println("Available Permissions: " + availableAuths);

        return availableAuths;
    }
}
