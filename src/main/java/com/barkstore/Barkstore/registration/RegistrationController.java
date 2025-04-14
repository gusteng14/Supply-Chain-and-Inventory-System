package com.barkstore.Barkstore.registration;

import com.barkstore.Barkstore.appuser.MyUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(path = "/api/v1/registration")
//@AllArgsConstructor
public class RegistrationController {
    @Autowired
    private final RegistrationService registrationService;
    @Autowired
    private final MyUserService userService;

    public RegistrationController(RegistrationService registrationService, MyUserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    //    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String register(Model model) {
//        model.addAttribute("registrationRequest", new RegistrationRequest());
//        return "register";
//    }

    // REGISTRATION THRU POSTMAN (TEST ONLY)
//    @PostMapping
//    public String register(@Valid @RequestBody RegistrationRequest request) {
//        return registrationService.register(request);
//    }

    // REGISTRATION THRU FORMS
    @PostMapping(path = "test", value="test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String test(@Valid @RequestBody RegistrationRequest registrationRequest) throws IOException {
       //return registrationService.register(registrationRequest);
//        response.sendRedirect("/register");
        System.out.println(registrationRequest.getRoleRequest());
        System.out.println(registrationRequest.getFirstName());
//        System.out.println("File: " + file.getOriginalFilename());

        userService.signUpUser(registrationRequest);
        return null;

    }

//    @GetMapping(path = "confirm")
//    public String confirm(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
//        registrationService.confirmToken(token);
//        response.sendRedirect("/login");
//        return " ";
//    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
