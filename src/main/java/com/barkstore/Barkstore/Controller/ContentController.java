package com.barkstore.Barkstore.Controller;

import com.barkstore.Barkstore.appuser.MyUser;
import com.barkstore.Barkstore.appuser.MyUserRepository;
import com.barkstore.Barkstore.products.*;
import com.barkstore.Barkstore.registration.RegistrationRequest;
import com.barkstore.Barkstore.registration.RegistrationService;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenRepository;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenService;
import com.barkstore.Barkstore.requisition.*;
import com.barkstore.Barkstore.requisition.RequestHeader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
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
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ItemTypeRepo itemTypeRepo;
    @Autowired
    private ItemTypeService itemTypeService;
    @Autowired
    private HeaderService headerService;
    @Autowired
    private DetailsService detailsService;
    @Autowired
    private SupplyChainService supplyChainService;

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

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/product")
    public String viewProduct(Model model) {
        model.addAttribute("productRequest", new ProductRequest());
        List<Product> product = productRepo.findAll();
        List<Category> category = categoryRepo.findAll();
        List<ItemType> itemType = itemTypeRepo.findAll();
        model.addAttribute("product", product);
        model.addAttribute("category", category);
        model.addAttribute("itemType", itemType);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id).get();
        model.addAttribute("product", product);


        return "viewProduct";
    }

    @GetMapping(value = "/{productId}/product_image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long productId) {
        Optional<Product> productOptional = productRepo.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            byte[] imageBytes = java.util.Base64.getDecoder().decode(product.getImageData());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new byte[0], HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/product/create")
    public String createProduct(Model model, @ModelAttribute ProductRequest productRequest, @RequestParam("image") MultipartFile file) throws IOException {
        model.addAttribute("productRequest", productRequest);
        System.out.println("Create Product (Controller): ");
        System.out.println(file.getOriginalFilename());

        productService.createProductLob(productRequest, file);

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

        productRepo.deleteById(id);


        return "redirect:/product";
    }

    @GetMapping("/category")
    public String viewCategory(Model model) {
        model.addAttribute("categoryRequest", new CategoryRequest());
        List<Category> category = categoryRepo.findAll();
        model.addAttribute("category", category);
        return "category";
    }

    @PostMapping("/category/create")
    public String createCategory(Model model, @ModelAttribute CategoryRequest categoryRequest) {
        Category category = new Category();
        model.addAttribute("category", category);

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
//        category.setStock(productRequest.getStock());
//        product.setCost(productRequest.getCost());

        categoryRepo.save(category);

        return "redirect:/category";
    }

    @GetMapping("/category/{id}")
    public String viewCategory(@PathVariable Long id, Model model) {
        Category category = categoryRepo.findById(id).get();
        model.addAttribute("category", category);

        return "viewCategory";
    }

    @GetMapping("/category/edit")
    public String editCategory(Model model, @RequestParam Long id) {
        Category category = categoryRepo.findById(id).get();
        model.addAttribute("category", category);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName(category.getName());
        categoryRequest.setDescription(category.getDescription());
//        categoryRequest.setStock(product.getStock());
//        categoryRequest.setCost(product.getCost());


        model.addAttribute("categoryRequest", categoryRequest);
        return "editCategory";
    }

    @PostMapping("/category/edit")
    public String updateCategory(Model model, @RequestParam Long id, @ModelAttribute CategoryRequest categoryRequest) {
        Category category = categoryRepo.findById(id).get();
        model.addAttribute("category", category);

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
//        category.setStock(productRequest.getStock());
//        category.setCost(productRequest.getCost());


        categoryRepo.save(category);
//        productService.lowInventoryNotif(product);

        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategoryById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        categoryRepo.deleteById(id);
        return "redirect:/category";
    }

    @GetMapping("/itemType")
    public String viewItemType(Model model) {
        model.addAttribute("itemTypeRequest", new ItemTypeRequest());
        List<ItemType> itemType = itemTypeRepo.findAll();
        model.addAttribute("itemType", itemType);
        return "itemType";
    }

    @PostMapping("/itemType/create")
    public String createItemType(Model model, @ModelAttribute ItemTypeRequest itemTypeRequest) {
        ItemType itemType = new ItemType();
        model.addAttribute("itemType", itemType);

        System.out.println("Item Type: " + itemTypeRequest.getName());
        itemType.setName(itemTypeRequest.getName());


//        itemType.setDescription(itemTypeRequest.getDescription());
//        itemType.setStock(itemTypeRequest.getStock());
//        itemType.setCost(itemTypeRequest.getCost());

        itemTypeRepo.save(itemType);

//        itemTypeService.createItemType(itemTypeRequest);

        return "redirect:/itemType";
    }

    @GetMapping("/itemType/{id}")
    public String viewItemType(@PathVariable Long id, Model model) {
        ItemType itemType = itemTypeRepo.findById(id).get();
        model.addAttribute("itemType", itemType);

        return "viewItemType";
    }

    @GetMapping("/itemType/edit")
    public String editItemType(Model model, @RequestParam Long id) {
        ItemType itemType = itemTypeRepo.findById(id).get();
        model.addAttribute("itemType", itemType);

        ItemTypeRequest itemTypeRequest = new ItemTypeRequest();
        itemTypeRequest.setName(itemType.getName());
//        productRequest.setDescription(product.getDescription());
//        productRequest.setStock(product.getStock());
//        productRequest.setCost(product.getCost());


        model.addAttribute("itemTypeRequest", itemTypeRequest);
        return "editItemType";
    }

    @PostMapping("/itemType/edit")
    public String updateItemType(Model model, @RequestParam Long id, @ModelAttribute ItemTypeRequest itemTypeRequest) {
        ItemType itemType = itemTypeRepo.findById(id).get();
        model.addAttribute("itemType", itemType);

        itemType.setName(itemTypeRequest.getName());

        itemTypeRepo.save(itemType);

        return "redirect:/itemType";
    }

    @GetMapping("/itemType/delete/{id}")
    public String deleteItemTypeById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        itemTypeRepo.deleteById(id);
        return "redirect:/itemType";
    }

    @GetMapping("/request")
    public String createRequest(Model model, @ModelAttribute HeaderDTO headerDTO, @ModelAttribute DetailsDTO detailsDTO) {
        List<Product> product = productRepo.findAll();
        model.addAttribute("product", product);
        return "requisitionPage";
    }

//    @GetMapping("/requests")
//    public String addRow(@RequestParam("addRow") String test) {
//        System.out.println("Add ka ng row be");
//        return "requisitionPage";
//    }

    @PostMapping("/request")
    public String sendRequest(@RequestParam("qty") String qty, @RequestParam("itemList") String itemList, @RequestParam("total") String total,
                              @ModelAttribute HeaderDTO headerDTO, @ModelAttribute DetailsDTO detailsDTO) {
        String[] stringArray1 = itemList.split(",");
        List<String> list1 = Arrays.asList(stringArray1);
        String[] stringArray2 = qty.split(",");
        List<String> list2 = Arrays.asList(stringArray2);
        String[] stringArray3 = total.split(",");
        List<String> list3 = Arrays.asList(stringArray3);
//        System.out.println("\nQuantities: " + list1);
//        System.out.println("Item List: " + list2);
//        System.out.println("Totals: " + list3);
//        System.out.println("Header Title: " + headerDTO.getRequestName());
//        System.out.println("Header Description: " + headerDTO.getRequestDescription());

        RequestHeader requestHeader = supplyChainService.saveHeader(headerDTO);
        supplyChainService.saveDetails(requestHeader, list1, list2, list3);

        return "redirect:/request";
    }

}
