package com.barkstore.Barkstore.Controller;

import com.barkstore.Barkstore.appuser.MyUser;
import com.barkstore.Barkstore.appuser.MyUserRepository;
import com.barkstore.Barkstore.appuser.UpdateUserRequest;
import com.barkstore.Barkstore.products.Product;
import com.barkstore.Barkstore.products.ProductRepo;
import com.barkstore.Barkstore.products.ProductRequest;
import com.barkstore.Barkstore.products.ProductService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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

    @GetMapping("/verified")
    public String verifiedPage() {
        return "verified";
    }

    @GetMapping("/verificationFailed")
    public String verificationFailedPage() {
        return "verificationFailed";
    }

    //    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    @Secured("ADMIN")
    @GetMapping("/employee")
    public String register(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        List<MyUser> user = repository.findAll();
        model.addAttribute("user", user);
        return "employee";
    }

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

    @GetMapping("/employee/delete/{id}")
    public String deleteUserById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        //tokenService.deleteByUserId(id);
        repository.deleteById(id);
        System.out.println("are u here hello " );


        return "redirect:/employee";
    }

}
