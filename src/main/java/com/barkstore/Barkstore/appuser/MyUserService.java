package com.barkstore.Barkstore.appuser;

import com.barkstore.Barkstore.Email.EmailSender;
import com.barkstore.Barkstore.registration.EmailValidator;
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
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

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

        if (user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("username not found");
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())))
                .collect(Collectors.toList());

        System.out.print("Authorities:: " + authorities);

        authorities.addAll(user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).collect(Collectors.toList()));

        System.out.print("Role:: " + user.getRoles());

        return new AuthenticatedUser(user, authorities);
    }

    public MyUser signUpUser(MyUser user) throws IOException {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        Role roles = roleRepository.findByName(user.getRoleRequest()).get();
        user.setRoles(Collections.singleton(roles));



        String token = UUID.randomUUID().toString();
        System.out.println("Token: " + token);
        user.setVerificationCode(token);
        userRepository.save(user);

        String link = "http://websike.xyz:8080/api/v1/registration/confirm?token=" + token;

        emailSender.send(user.getEmail(), buildEmail(user.getFirstName(), link), "Confirm your email");
        return null;
    }

    public boolean verifyUser (String verificationCode) {
        System.out.println("Token before verification: " + verificationCode);

        MyUser user = userRepository.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            System.out.print("Ayaw ma enable tol");
            return false;
        } else {
            System.out.print("Na enable ko na tol");
            userRepository.enable(user.getId());

            return true;
        }
    }

    public Set<Authority> getAuthoritiesFromRequest(RoleRequest roleRequest) {
        return roleRequest.getAuthorities().stream().map(authorityRequest -> {
            return getOrCreateAuthority(authorityRequest);
        }).collect(Collectors.toSet());
    }

    public Authority getOrCreateAuthority(AuthorityRequest authorityRequest) {
        return authorityRepository.findByName(authorityRequest.getName()).orElseGet(() -> {
            Authority auth = new Authority();
            auth.setName(authorityRequest.getName());
            return authorityRepository.save(auth);
        });
    }

    public Role getOrCreateRole(RoleRequest roleRequest) {
        return roleRepository.findByName(roleRequest.getName()).orElseGet(() -> {
            Role r = new Role();
            r.setName(roleRequest.getName());
            return roleRepository.save(r);
        });
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi,</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n  <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }




}
