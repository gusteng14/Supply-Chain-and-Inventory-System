package com.barkstore.Barkstore.Controller;

import com.barkstore.Barkstore.appuser.*;
import com.barkstore.Barkstore.products.*;
import com.barkstore.Barkstore.registration.RegistrationRequest;
import com.barkstore.Barkstore.registration.RegistrationService;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenRepository;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenService;
import jakarta.validation.Valid;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
public class AdminController {
    @Autowired
    private MyUserRepository repository;
    @Autowired
    private ConfirmationTokenRepository tokenRepository;
    @Autowired
    private ConfirmationTokenService tokenService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductService productService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleService roleService;

    @GetMapping("/verified")
    public String verifiedPage() {
        return "verified";
    }

    @GetMapping("/verificationFailed")
    public String verificationFailedPage() {
        return "verificationFailed";
    }

    @PreAuthorize("hasAuthority('READ_ROLE_PERM')")
    @GetMapping("/roles")
    public String roles(Model model) {
        model.addAttribute("roleRequest", new RoleRequest());
        model.addAttribute("authorityRequest", new AuthorityRequest());
        List<Role> role = roleRepository.findAll();
        List<Authority> authority = authorityRepository.findAll();
        model.addAttribute("role", role);
        model.addAttribute("authority", authority);
        return "role";
    }

    @PreAuthorize("hasAuthority('READ_ROLE_PERM')")
    @GetMapping("/role/{id}")
    public String viewRole(Model model, @PathVariable Long id) {
        Role role = roleRepository.findById(id).get();
        model.addAttribute("role", role);

        List<Authority> authority = authorityRepository.findAll();
        model.addAttribute("authority", authority);

        RoleRequest roleRequest = new RoleRequest();
        roleRequest.setName(role.getName());

        model.addAttribute("roleRequest", roleRequest);
//        System.out.println(user.getRoles().toString());

        return "viewRole";
    }

    @PreAuthorize("hasAuthority('CREATE_ROLE_PERM')")
    @PostMapping("/role/create")
    public String createRole(Model model, @ModelAttribute RoleRequest roleRequest, @RequestParam("authority") String authority) {
        Role role = new Role();
        model.addAttribute("role", role);

        String[] stringArray = authority.split(",");
        List<String> list = Arrays.asList(stringArray);
        System.out.println("Role name: " + roleRequest.getName());
        System.out.println(roleRequest.getName() + "'s" + " permissions: " + list);

        role.setName(roleRequest.getName());
//        roleRepository.save(role);

        roleService.createRole(roleRequest, list);

        return "redirect:/roles";
    }

    @PreAuthorize("hasAuthority('UPDATE_ROLE_PERM')")
    @GetMapping("/role/edit")
    public String editRole(Model model, @RequestParam Long id, @ModelAttribute RoleRequest roleRequest) {
        List<Authority> authority = authorityRepository.findAll();
        model.addAttribute("authority", authority);

        Role role = roleRepository.findById(id).get();
        model.addAttribute("role", role);

        roleRequest.setName(role.getName());

        System.out.println("Currently updating: " + role.getName() + " role" + " permissions.");



        List<String> avilableAuths = roleService.showAuthForUpdate(id);
        model.addAttribute("avilableAuths", avilableAuths);

        return "editRole";
    }

    @PreAuthorize("hasAuthority('UPDATE_ROLE_PERM')")
    @PostMapping("/role/edit")
    public String updateRole(Model model, @RequestParam Long id, @ModelAttribute RoleRequest roleRequest, @RequestParam("authority") String authority) {
        Role role = roleRepository.findById(id).get();
        model.addAttribute("role", role);

        roleService.showSavedRoleAuth(id);

        String[] stringArray = authority.split(",");
        List<String> list = Arrays.asList(stringArray);
        System.out.println("Role name: " + role.getName());
        System.out.println(role.getName() + "'s" + " new permissions: " + list);

        Set<Authority> authorities = roleService.saveAuthorityToRole(list);
//        for (Authority auth : role.getAuthorities()) {
//            authorities.add(auth);
//        }
        role.setAuthorities(authorities);
        roleRepository.save(role);

        return "redirect:/roles";
    }

    @PreAuthorize("hasAuthority('DELETE_ROLE_PERM')")
    @GetMapping("/role/delete/{id}")
    public String deleteRoleById(@PathVariable(name="id") Long id) {
        roleRepository.deleteById(id);
        return "redirect:/roles";
    }

    @PreAuthorize("hasAuthority('READ_USER_PERM')")
    @GetMapping("/employee")
    public String register(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        List<Role> role = roleRepository.findAll();
        List<MyUser> user = repository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("role", role);
        return "employee";
    }

    @PreAuthorize("hasAuthority('READ_USER_PERM')")
    @GetMapping("/employee/{id}")
    public String viewEmployee(Model model, @PathVariable Long id) {
        MyUser user = repository.findById(id).get();
        model.addAttribute("user", user);

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName(user.getFirstName());
        registrationRequest.setLastName(user.getLastName());
        registrationRequest.setMiddleName(user.getMiddleName());
        registrationRequest.setContactNo(user.getContactNo());
        registrationRequest.setEmail(user.getEmail());
        registrationRequest.setUsername(user.getUsername());
        registrationRequest.setPassword(user.getPassword());

        model.addAttribute("registrationRequest", registrationRequest);
        System.out.println(user.getRoles().toString());

        return "viewEmployee";
    }

//    @GetMapping(value = "/{employeeId}/emp_image")
//    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable Long employeeId) {
//        Optional<MyUser> userOptional = repository.findById(employeeId);
//        if (userOptional.isPresent()) {
//            MyUser user = userOptional.get();
//            byte[] imageBytes = java.util.Base64.getDecoder().decode(user.getImageData());
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(new byte[0], HttpStatus.NOT_FOUND);
//        }
//    }

    @PreAuthorize("hasAuthority('UPDATE_USER_PERM')")
    @GetMapping("/employee/edit")
    public String editEmployee(Model model, @RequestParam Long id) {
        MyUser user = repository.findById(id).get();
        model.addAttribute("user", user);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setFirstName(user.getFirstName());
        updateUserRequest.setLastName(user.getLastName());
        updateUserRequest.setMiddleName(user.getMiddleName());
        updateUserRequest.setContactNo(user.getContactNo());
        updateUserRequest.setEmail(user.getEmail());

        model.addAttribute("updateUserRequest", updateUserRequest);
        return "editEmployee";
    }

    @PreAuthorize("hasAuthority('UPDATE_USER_PERM')")
    @PostMapping("/employee/edit")
    public String updateEmployee(Model model, @RequestParam Long id, @Valid @ModelAttribute UpdateUserRequest updateUserRequest) {
        MyUser user = repository.findById(id).get();
        model.addAttribute("user", user);

        user.setFirstName(updateUserRequest.getFirstName());
        user.setMiddleName(updateUserRequest.getMiddleName());
        user.setLastName(updateUserRequest.getLastName());
        user.setContactNo(updateUserRequest.getContactNo());
        user.setEmail(updateUserRequest.getEmail());
        repository.save(user);

        return "redirect:/employee";
    }

    @PreAuthorize("hasAuthority('DELETE_USER_PERM')")
    @GetMapping("/employee/delete/{id}")
    public String deleteUserById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        repository.deleteById(id);

        return "redirect:/employee";
    }

}
