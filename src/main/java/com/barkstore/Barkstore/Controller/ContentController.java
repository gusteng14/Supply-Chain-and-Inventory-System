package com.barkstore.Barkstore.Controller;

import com.barkstore.Barkstore.appuser.MyUser;
import com.barkstore.Barkstore.appuser.MyUserRepository;
import com.barkstore.Barkstore.products.Product;
import com.barkstore.Barkstore.products.ProductRepo;
import com.barkstore.Barkstore.products.ProductRequest;
import com.barkstore.Barkstore.products.ProductService;
import com.barkstore.Barkstore.registration.RegistrationRequest;
import com.barkstore.Barkstore.registration.RegistrationService;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenRepository;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {
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

    @GetMapping("/login")
    public String login() {
        return "login2";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/index")
    public String home() {
        return "index";
    }



//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/product")
    public String viewProduct(Model model) {
        model.addAttribute("productRequest", new ProductRequest());
        List<Product> product = productRepo.findAll();
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping("/product/create")
    public String createProduct(Model model, @ModelAttribute ProductRequest productRequest) {
        Product product = new Product();
        model.addAttribute("product", product);

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setStock(productRequest.getStock());
        product.setCost(productRequest.getCost());
        productRepo.save(product);

        return "redirect:/product";
    }

    @GetMapping("/product/edit")
    public String editProduct(Model model, @RequestParam Long id) {
        Product product = productRepo.findById(id).get();
        model.addAttribute("product", product);

        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(product.getName());
        productRequest.setDescription(product.getDescription());
        productRequest.setStock(product.getStock());
        productRequest.setCost(product.getCost());


        model.addAttribute("productRequest", productRequest);
        return "editProduct";
    }

    @PostMapping("/product/edit")
    public String updateProduct(Model model, @RequestParam Long id, @ModelAttribute ProductRequest productRequest) {
        System.out.println("are u here hello " );
        Product product = productRepo.findById(id).get();
        model.addAttribute("product", product);

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setStock(productRequest.getStock());
        product.setCost(productRequest.getCost());


        productRepo.save(product);
        productService.lowInventoryNotif(product);

        return "redirect:/product";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProductById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        productRepo.deleteById(id);
        System.out.println("are u here hello " );


        return "redirect:/product";
    }

}
