package com.barkstore.Barkstore.Security;

import com.barkstore.Barkstore.appuser.MyUser;
import com.barkstore.Barkstore.appuser.MyUserRepository;
import com.barkstore.Barkstore.appuser.Role;
import com.barkstore.Barkstore.appuser.RoleRepository;
import com.barkstore.Barkstore.registration.RegistrationRequest;
import com.barkstore.Barkstore.registration.token.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService /*implements UserDetailsService*/ {

    private MyUserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        MyUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
//        return new User(user.getUsername(), user.getPassword(),mapRolesToAuthorities(user.getRoles()));
//    }

    @Autowired
    public CustomUserDetailsService(MyUserRepository userRepository) {
        this.userRepository = userRepository;
    }



//    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }

//    public String signUpUser(RegistrationRequest registrationRequest) {
//        boolean userExist = userRepository.findByUsername(registrationRequest.getUsername()).isPresent();
//
//        if(userExist) {
//            throw new IllegalStateException("email already taken");
//        }
//
//        //System.out.println(user.getRoleRequest());
//        MyUser user = new MyUser(
//                registrationRequest.getFirstName(),
//                registrationRequest.getLastName(),
//                registrationRequest.getMiddleName(),
//                registrationRequest.getContactNo(),
//                registrationRequest.getUsername(),
//                registrationRequest.getEmail(),
//                registrationRequest.getPassword()
//        );
//
//        Role roles = roleRepository.findByName(registrationRequest.getRole()).get();
//        user.setRoles(Collections.singleton(roles));
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encodedPassword);
//        userRepository.save(user);
//
////        String token = UUID.randomUUID().toString();
////        ConfirmationToken confirmationToken = new ConfirmationToken(
////                token,
////                LocalDateTime.now(),
////                LocalDateTime.now().plusMinutes(15),
////                user
////        );
//
////        confirmationTokenService.saveConfirmationToken(confirmationToken);
////        return token;
//        return null;
//    }


}
