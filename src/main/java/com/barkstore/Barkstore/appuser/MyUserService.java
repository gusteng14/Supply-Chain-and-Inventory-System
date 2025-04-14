package com.barkstore.Barkstore.appuser;

import com.barkstore.Barkstore.registration.RegistrationRequest;
import com.barkstore.Barkstore.registration.token.ConfirmationToken;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
public class MyUserService implements UserDetailsService {
    @Autowired
    private final MyUserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private final AuthorityRepository authorityRepository;

    private final static String USER_NOT_FOUND_MSG = "user with email s% not found";

    public MyUserService(MyUserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsername(username);
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())))
                .collect(Collectors.toList());

        System.out.print("Authorities:: " + authorities);

        authorities.addAll(user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList()));

        System.out.print("Role:: " + user.getRoles());

        return new AuthenticatedUser(user, authorities);
    }

    public MyUser signUpUser(RegistrationRequest registrationRequest) throws IOException {
        System.out.println("First Name: "  + registrationRequest.getFirstName());
        System.out.println("Last Name: "  + registrationRequest.getLastName());
        System.out.println("Middle Name: "  + registrationRequest.getMiddleName());
        System.out.println("Email: "  + registrationRequest.getEmail());
        System.out.println("Contact No: "  + registrationRequest.getContactNo());
        System.out.println("Username: "  + registrationRequest.getUsername());
        System.out.println("CHOSEN ROLE:::"  + registrationRequest.getRoleRequest());

        MyUser user = new MyUser();
        user.setFirstName(registrationRequest.getFirstName());
        user.setMiddleName(registrationRequest.getMiddleName());
        user.setLastName(registrationRequest.getLastName());
        user.setContactNo(registrationRequest.getContactNo());
        user.setEmail(registrationRequest.getEmail());
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(registrationRequest.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
//        user.setImageData(Base64.getEncoder().encodeToString(file.getBytes()));



        Role roles = roleRepository.findByName(registrationRequest.getRoleRequest()).get();
        user.setRoles(Collections.singleton(roles));



        return userRepository.save(user);
    }

    private Set<Authority> getAuthoritiesFromRequest(RoleRequest roleRequest) {
        return roleRequest.getAuthorities().stream().map(authorityRequest -> {
            return getOrCreateAuthority(authorityRequest);
        }).collect(Collectors.toSet());
    }

    private Authority getOrCreateAuthority(AuthorityRequest authorityRequest) {
        return authorityRepository.findByName(authorityRequest.getName()).orElseGet(() -> {
            Authority auth = new Authority();
            auth.setName(authorityRequest.getName());
            return authorityRepository.save(auth);
        });
    }

    private Role getOrCreateRole(RoleRequest roleRequest) {
        return roleRepository.findByName(roleRequest.getName()).orElseGet(() -> {
            Role r = new Role();
            r.setName(roleRequest.getName());
            return roleRepository.save(r);
        });
    }




}
