package com.barkstore.Barkstore.Controller;

import com.barkstore.Barkstore.Supplier.Supplier;
import com.barkstore.Barkstore.Supplier.SupplierRepository;
import com.barkstore.Barkstore.Supplier.SupplierRequest;
import com.barkstore.Barkstore.Supplier.SupplierService;
import com.barkstore.Barkstore.appuser.MyUser;
import com.barkstore.Barkstore.appuser.MyUserRepository;
import com.barkstore.Barkstore.audit.CateogryAuditDTO;
import com.barkstore.Barkstore.audit.ItemTypeAuditDTO;
import com.barkstore.Barkstore.audit.ProductAuditDTO;
import com.barkstore.Barkstore.audit.SupplierAuditDTO;
import com.barkstore.Barkstore.pos.*;
import com.barkstore.Barkstore.products.*;
import com.barkstore.Barkstore.registration.RegistrationRequest;
import com.barkstore.Barkstore.registration.RegistrationService;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenRepository;
import com.barkstore.Barkstore.registration.token.ConfirmationTokenService;
import com.barkstore.Barkstore.requisition.*;
import com.barkstore.Barkstore.requisition.RequestHeader;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private OrderHeaderRepository orderHeaderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private POSService posService;



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
    public String dashboard(Model model) throws FileNotFoundException {
        float totalSales = posService.totalSales();
        String formattedTotalSale = String.format("%.2f", totalSales);

        long noOfSuppliers = supplierService.activeSupplierCount();
        long noOfProducts = productService.activeProductsCount();
        long noOfLowStockProducts = productService.getCountOfLowStockProducts();
        long totalOrders = orderHeaderRepository.count();
        List<Product> lowStockProducts = productService.getLowStockProducts();
        List<Product> top5Products = productService.fiveBestSeller();

        model.addAttribute("totalSales", formattedTotalSale);
        model.addAttribute("noOfSuppliers", noOfSuppliers);
        model.addAttribute("noOfProducts", noOfProducts);
        model.addAttribute("noOfLowStockProducts", noOfLowStockProducts);
        model.addAttribute("top5Products", top5Products);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("lowStockProducts", lowStockProducts);


        return "dashboard";
    }


//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/product")
    public String viewProduct(Model model) {
        model.addAttribute("productRequest", new ProductRequest());
        List<Product> product = productRepo.findAllActive();
        List<Category> category = categoryRepo.findAllActive();
        List<ItemType> itemType = itemTypeRepo.findAllActive();
        List<Product> deletedFields = productService.getSoftDeletesProduct();
        List<ProductAuditDTO> audits = productService.getAllAuditsProduct();

        model.addAttribute("product", product);
        model.addAttribute("category", category);
        model.addAttribute("itemType", itemType);
        model.addAttribute("deletedFields", deletedFields);
        model.addAttribute("audits", audits);
        return "product";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id).get();
        Revisions<Long, Product> logs = productRepo.findRevisions(id);

        model.addAttribute("logs", logs);
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

        if (productRequest.getStock() <= productRequest.getReorderPoint()) {
            productRequest.setIsLowStock(true);
        } else {
            productRequest.setIsLowStock(false);
        }
        productService.createProductLob(productRequest, file);

        return "redirect:/product";
    }

    @GetMapping("/product/restock/{id}/{qty}")
    public String restockProduct(@PathVariable("id") Long id, @PathVariable("qty") int qty) {
        Product product = productRepo.findById(id).get();
        product.setStock(product.getStock() + qty);
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
        productRequest.setReorderPoint(product.getReorderPoint());
        productRequest.setDefaultRestockQuantity(product.getDefaultRestockQuantity());


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
        product.setReorderPoint(productRequest.getReorderPoint());
        product.setDefaultRestockQuantity(productRequest.getDefaultRestockQuantity());
        if (productRequest.getStock() <= productRequest.getReorderPoint()) {
            product.setIsLowStock(true);
        } else {
            product.setIsLowStock(false);
        }


        productRepo.save(product);
        productService.lowInventoryNotif(product);

        return "redirect:/product";
    }

    @GetMapping("/product/softdelete/{id}")
    public String softDeleteProductById(@PathVariable(name="id") Long id) {
        productService.softDeleteProduct(id);
        return "redirect:/product";
    }

    @GetMapping("/product/restore/{id}")
    public String restoreProductById(@PathVariable(name="id") Long id) {
        productService.restoreRecordProduct(id);
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
        List<Category> category = categoryRepo.findAllActive();
        List<Category> deletedFields = productService.getSoftDeletesCategory();
        List<CateogryAuditDTO> audits = productService.getAllAuditsCategory();


        model.addAttribute("category", category);
        model.addAttribute("deletedFields", deletedFields);
        model.addAttribute("audits", audits);


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
        Revisions<Long, Category> logs = categoryRepo.findRevisions(id);

        model.addAttribute("category", category);
        model.addAttribute("logs", logs);

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



    @GetMapping("/category/restore/{id}")
    public String restoreCategoryById(@PathVariable(name="id") Long id) {
        productService.restoreRecordCategory(id);
        return "redirect:/category";
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategoryById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        categoryRepo.deleteById(id);
        return "redirect:/category";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GetMapping("/transaction")
    public String getTransactions(Model model) {
//        model.addAttribute("categoryRequest", new CategoryRequest());
        List<OrderHeader> transactions = orderHeaderRepository.findAll();
        List<OrderDetail> details = orderDetailsRepository.findAll();
        model.addAttribute("transactions", transactions);
        model.addAttribute("details", details);
        return "transaction";
    }

    @GetMapping("/transaction/{id}")
    public String viewTransaction(@PathVariable Long id, Model model) {
        OrderHeader orderHeader = orderHeaderRepository.findById(id).get();
        List<OrderDetail> orderDetails = orderDetailsRepository.findByHeaderId_Id(id);
        model.addAttribute("orderHeader", orderHeader);
        model.addAttribute("orderDetails", orderDetails);

        return "viewTransaction";
    }

    @PreAuthorize("hasAuthority('VOID_TRANSACTION_PERM')")
    @GetMapping("/transaction/void/{id}")
    public String voidReceipt(@PathVariable Long id) {
        OrderHeader orderHeader = orderHeaderRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        List<OrderDetail> orderDetails = orderDetailsRepository.findByHeaderId_Id(id);
        posService.voidReceipt(orderHeader, orderDetails);

        return "redirect:/transaction";
    }

    @PreAuthorize("hasAuthority('VOID_TRANSACTION_PERM')")
    @GetMapping("/transaction/validate/{id}")
    public String validateReceipt(@PathVariable Long id) {
        OrderHeader orderHeader = orderHeaderRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        List<OrderDetail> orderDetails = orderDetailsRepository.findByHeaderId_Id(id);
        posService.validateReceipt(orderHeader, orderDetails);

        return "redirect:/transaction";
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @GetMapping("/itemType")
    public String viewItemType(Model model) {
        model.addAttribute("itemTypeRequest", new ItemTypeRequest());
        List<ItemType> itemType = itemTypeRepo.findAllActive();
        List<ItemType> deletedFields = productService.getSoftDeletesItemType();
        List<ItemTypeAuditDTO> audits = productService.getAllAuditsItemType();

        model.addAttribute("itemType", itemType);
        model.addAttribute("deletedFields", deletedFields);
        model.addAttribute("audits", audits);
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



    @GetMapping("/itemType/restore/{id}")
    public String restoreItemTypeById(@PathVariable(name="id") Long id) {
        productService.restoreRecordItemType(id);
        return "redirect:/itemType";
    }

    @GetMapping("/itemType/delete/{id}")
    public String deleteItemTypeById(@PathVariable(name="id") Long id) {
        System.out.println("C ID IS: " + id);
        itemTypeRepo.deleteById(id);
        return "redirect:/itemType";
    }

    @GetMapping("/supplier")
    public String getAllSuppliers(Model model) {
        model.addAttribute("supplierRequest", new SupplierRequest());
        List<Supplier> supplier = supplierRepository.findAllActive();
        List<Supplier> deletedFields = supplierService.getSoftDeletes();

        List<SupplierAuditDTO> audits = supplierService.getAllAudits();

        model.addAttribute("supplier", supplier);
        model.addAttribute("deletedFields", deletedFields);
        model.addAttribute("audits", audits);
        return "supplier";
    }

    @PostMapping("/supplier/create")
    public String createSupplier(Model model, @ModelAttribute SupplierRequest supplierRequest) {
        Supplier supplier = new Supplier();
        model.addAttribute("supplierRequest", supplierRequest);
        supplierService.createSupplier(supplierRequest);
        return "redirect:/supplier";
    }

    @GetMapping("/supplier/{id}")
    public String viewSupplier(@PathVariable Long id, Model model) {
        Supplier supplier = supplierRepository.findById(id).get();
        Revisions<Long, Supplier> logs = supplierRepository.findRevisions(id);

        model.addAttribute("supplier", supplier);
        model.addAttribute("logs", logs);

        return "viewSupplier";
    }

    @GetMapping("/supplier/edit")
    public String editSupplier(Model model, @RequestParam Long id) {
        Supplier supplier = supplierRepository.findById(id).get();
        model.addAttribute("supplier", supplier);
        SupplierRequest supplierRequest = new SupplierRequest();

        supplierRequest.setSupplierNo(supplier.getSupplierNo());
        supplierRequest.setName(supplier.getName());
        supplierRequest.setContactNo(supplier.getContactNo());
        supplierRequest.setEmail(supplier.getEmail());
        supplierRequest.setAddressLine1(supplier.getAddressLine1());
        supplierRequest.setAddressLine2(supplier.getAddressLine2());
        supplierRequest.setAddressLine3(supplier.getAddressLine3());
        supplierRequest.setAgentName(supplier.getAgentName());
        supplierRequest.setAgentContactNo(supplier.getAgentContactNo());

        model.addAttribute("supplierRequest", supplierRequest);

        return "editSupplier";
    }

    @PostMapping("/supplier/edit")
    public String updateSupplier(Model model, @RequestParam Long id, @ModelAttribute SupplierRequest supplierRequest) {
        Supplier supplier = supplierRepository.findById(id).get();
        model.addAttribute("supplier", supplier);

        supplierService.editSupplier(supplier, supplierRequest);

        return "redirect:/supplier";
    }

    @GetMapping("/supplier/softdelete/{id}")
    public String softDeleteSupplierById(@PathVariable(name="id") Long id) {
        supplierService.softDelete(id);
        return "redirect:/supplier";
    }

    @GetMapping("/supplier/restore/{id}")
    public String restoreSupplierById(@PathVariable(name="id") Long id) {
        supplierService.restoreRecord(id);
        return "redirect:/supplier";
    }

    @GetMapping("/supplier/delete/{id}")
    public String deleteSupplierById(@PathVariable(name="id") Long id) {
        supplierRepository.deleteById(id);
        return "redirect:/supplier";
    }

    @GetMapping("/supplier/logs")
    public String getSupplierLogs(Model model) {
        Revisions<Long, Supplier> supplier = supplierRepository.findRevisions(252L);
        model.addAttribute("supplier", supplier);
        return "supplierLogs";
    }

    @GetMapping("/request")
    public String createRequest(Model model, @ModelAttribute HeaderDTO headerDTO, @ModelAttribute DetailsDTO detailsDTO) {
        List<Product> product = productRepo.findAll();
        model.addAttribute("product", product);
        return "requisitionPage";
    }

    @GetMapping("/requestLowStock")
    public String createLowStockRequest(Model model, @ModelAttribute HeaderDTO headerDTO, @ModelAttribute DetailsDTO detailsDTO) {
        List<Product> product = productRepo.findAll();
        List<Product> lowStockProducts = productService.getLowStockProducts();
        model.addAttribute("product", product);
        model.addAttribute("lowStockProducts", lowStockProducts);
        return "requisitionPage2";
    }

    @PostMapping("/request")
    public String sendRequest(@RequestParam("qty") String qty, @RequestParam("itemList") String itemList, @RequestParam("total") String total,
                              @ModelAttribute HeaderDTO headerDTO, @ModelAttribute DetailsDTO detailsDTO) {
        String[] stringArray1 = itemList.split(",");
        List<String> list1 = Arrays.asList(stringArray1);
        String[] stringArray2 = qty.split(",");
        List<String> list2 = Arrays.asList(stringArray2);
        String[] stringArray3 = total.split(",");
        List<String> list3 = Arrays.asList(stringArray3);

        System.out.println(list1);
        System.out.println(list2);
        System.out.println(list3);

        RequestHeader requestHeader = supplyChainService.saveHeader(headerDTO);
        supplyChainService.saveDetails(requestHeader, list1, list2, list3);

        return "redirect:/request";
    }

    @GetMapping("/pos")
    public String getPOS(Model model) {
        List<Product> products = productRepo.findAll();
        List<Category> categories = categoryRepo.findAll();
        List<OrderHeader> orders = orderHeaderRepository.findByCreatedOn(LocalDate.now());

        model.addAttribute("product", products);
        model.addAttribute("category", categories);
        model.addAttribute("orders", orders);

        return "pos";
    }

    @PostMapping("/pos")
    public String submitOrder(@RequestParam("item") String item, @RequestParam("qty") String qty, @RequestParam("unitPrice") String unitPrice,
                              @RequestParam("lptotal") String listPriceTotal, @RequestParam("total") String total) {
        System.out.println("Item: " + item);
        System.out.println("Qty: " + qty);
        System.out.println("Totals: " + listPriceTotal);

        String[] stringArray1 = item.split(",");
        List<String> list1 = Arrays.asList(stringArray1);
        String[] stringArray2 = qty.split(",");
        List<String> list2 = Arrays.asList(stringArray2);
        String[] stringArray3 = listPriceTotal.split(",");
        List<String> list3 = Arrays.asList(stringArray3);
        String[] stringArray4 = unitPrice.split(",");
        List<String> list4 = Arrays.asList(stringArray3);

        OrderHeader header = posService.saveHeader(total);
        posService.saveDetails(header, list1, list2, list3, list4);

        return "redirect:/pos";
    }

}
