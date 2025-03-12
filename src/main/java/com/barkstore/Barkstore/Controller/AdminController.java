package com.barkstore.Barkstore.Controller;

import com.barkstore.Barkstore.appuser.MyUser;
import com.barkstore.Barkstore.appuser.MyUserRepository;
import com.barkstore.Barkstore.products.ProductRepo;
import com.barkstore.Barkstore.products.ProductService;
import com.barkstore.Barkstore.registration.RegistrationRequest;
import com.barkstore.Barkstore.registration.RegistrationService;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenRepository;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class AdminController {
    @Autowired
    private MyUserRepository repository;
    private ConfirmationTokenRepository tokenRepository;
    @Autowired
    private ConfirmationTokenService tokenService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductService productService;

    //    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    @Secured("ADMIN")
    @GetMapping("/employee")
    public String register(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        List<MyUser> user = repository.findAll();
        model.addAttribute("user", user);
        return "employee";
    }

    @GetMapping("/employee/edit")
    public String editEmployee(Model model, @RequestParam Long id) {
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
        return "editEmployee";
    }

    @PostMapping("/employee/edit")
    public String updateEmployee(Model model, @RequestParam Long id, @Valid @ModelAttribute RegistrationRequest registrationRequest) {
        MyUser user = repository.findById(id).get();
        model.addAttribute("user", user);

        user.setFirstName(registrationRequest.getFirstName());
        user.setMiddleName(registrationRequest.getMiddleName());
        user.setLastName(registrationRequest.getLastName());
        user.setContactNo(registrationRequest.getContactNo());
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        repository.save(user);

        return "redirect:/employee";
    }

    @GetMapping("/employee/delete/{id}")
    public String deleteUserById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        //tokenService.deleteByUserId(id);
        repository.deleteById(id);
        System.out.println("are u here hello " );


        return "redirect:/employee";
    }

}
