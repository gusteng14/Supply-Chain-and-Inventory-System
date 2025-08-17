package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.Email.EmailSender;
import com.barkstore.Barkstore.Supplier.Supplier;
import com.barkstore.Barkstore.audit.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Service
//@AllArgsConstructor

public class ProductService {
    @Autowired
    private ProductRepo repo;
    @Autowired
    private final EmailSender emailSender;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ItemTypeRepo itemTypeRepo;
    @PersistenceContext
    private EntityManager entityManager;

    public ProductService(ProductRepo repo, EmailSender emailSender) {
        this.repo = repo;
        this.emailSender = emailSender;
    }

    public Product createProductLob(ProductRequest productRequest, MultipartFile file) throws IOException {
        Product product = new Product();
        product.setImageData(Base64.getEncoder().encodeToString(file.getBytes()));
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setStock(productRequest.getStock());
        product.setCost(productRequest.getCost());
        product.setReorderPoint(productRequest.getReorderPoint());
        product.setDefaultRestockQuantity(productRequest.getDefaultRestockQuantity());

        if(product.getStock() <= product.getReorderPoint()) {
            product.setIsLowStock(true);
        } else {
            product.setIsLowStock(false);
        }

        Category category = categoryRepo.findByName(productRequest.getCategory()).get();
        product.setCategory(category);

        ItemType itemType = itemTypeRepo.findByName(productRequest.getItemType()).get();
        product.setItemType(itemType);

        repo.save(product);
        return product;
    }

    public void uploadFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try {
            InputStream inputStream = file.getInputStream();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public List<Product> getLowStockProducts() {
        List<Product> products = repo.findAllActive();
        List<Product> lowStockProducts = new ArrayList<>();

        for (Product product : products) {
            if (product.getStock() < product.getReorderPoint()) {
                lowStockProducts.add(product);
            }
        }

        return lowStockProducts;
    }

    public long getCountOfLowStockProducts() {
        List<Product> products = repo.findAllActive();
        long count = 0;

        for (Product product : products) {
            if (product.getStock() < product.getReorderPoint()) {
                count++;
            }
        }

        return count;
    }

    public String lowInventoryNotif (Product product) {
        int stock = product.getStock();
        String item = product.getName();

        if (stock < product.getReorderPoint()) {
            emailSender.send("gusteng14@gmail.com", buildEmail("Augustine", item, stock), "Low Inventory Notice");
        }
        return "Stock: ";
    }

    private String buildEmail(String name, String item, int stock) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Low inventory notice</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> An item has reached a low level of stock.  </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> </p></blockquote>\n Item:" + item + "  <p>Stock: " + stock + "</p>" +
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

    public List<Product> fiveBestSeller() {
        List<Product> products = repo.findTop5ByOrderByTotalQuantitySoldDesc();
        return products;
    }

    public ResponseEntity<String> softDeleteCategory(Long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        List<Product> productWithCategory = repo.findByCategory_Id(id);

        if (!productWithCategory.isEmpty()) {
            return new ResponseEntity<>("This category is being referenced by a product.", HttpStatus.BAD_REQUEST);
        }

        category.setDeleted(true);
        categoryRepo.save(category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<Category> findAllActiveCategory() {
        List<Category> active = categoryRepo.findAllActive();
        return active;
    }

    public List<Category> getSoftDeletesCategory() {
        List<Category> deletedFields = categoryRepo.findSoftDeletes();
        return deletedFields;
    }

    public void restoreRecordCategory(Long id) {
        Category category = categoryRepo.findById(id).get();
        category.setDeleted(false);
        categoryRepo.save(category);
    }

    public List<CateogryAuditDTO> getAllAuditsCategory() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(Category.class, false, true);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        List<CateogryAuditDTO> resultsToString = new ArrayList<>();

        for (Object[] row : results) {
            Category entity = (Category) row[0];
            UserRevisionEntity revisionEntity = (UserRevisionEntity) row[1];
            RevisionType revisionType = (RevisionType) row[2];

            CateogryAuditDTO dto = new CateogryAuditDTO();
            dto.setRevId(String.valueOf(revisionEntity.getRev()));
            dto.setAction(String.valueOf(revisionType));
            java.sql.Date timestamp = new java.sql.Date(revisionEntity.getRevtstmp());
            dto.setDate(String.valueOf(timestamp));
            dto.setCategoryId(String.valueOf(entity.getId()));
            dto.setCategoryName(entity.getName());
            dto.setUsername(revisionEntity.getUsername());
            resultsToString.add(dto);
        }
        return resultsToString;

    }

    public ResponseEntity<String> softDeleteItemType(Long id) {
        ItemType itemType = itemTypeRepo.findById(id).orElseThrow(() -> new RuntimeException("Item Type not found"));
        List<Product> productWithItemType = repo.findByItemType_Id(id);

        if (!productWithItemType.isEmpty()) {
            return new ResponseEntity<>("This item type is being referenced by a product.", HttpStatus.BAD_REQUEST);
        }

        itemType.setDeleted(true);
        itemTypeRepo.save(itemType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<ItemType> findAllActiveItemType() {
        List<ItemType> active = itemTypeRepo.findAllActive();
        return active;
    }

    public List<ItemType> getSoftDeletesItemType() {
        List<ItemType> deletedFields = itemTypeRepo.findSoftDeletes();
        return deletedFields;
    }

    public void restoreRecordItemType(Long id) {
        ItemType itemType = itemTypeRepo.findById(id).get();
        itemType.setDeleted(false);
        itemTypeRepo.save(itemType);
    }

    public List<ItemTypeAuditDTO> getAllAuditsItemType() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(ItemType.class, false, true);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        List<ItemTypeAuditDTO> resultsToString = new ArrayList<>();

        for (Object[] row : results) {
            ItemType entity = (ItemType) row[0];
            UserRevisionEntity revisionEntity = (UserRevisionEntity) row[1];
            RevisionType revisionType = (RevisionType) row[2];

            ItemTypeAuditDTO dto = new ItemTypeAuditDTO();
            dto.setRevId(String.valueOf(revisionEntity.getRev()));
            dto.setAction(String.valueOf(revisionType));
            java.sql.Date timestamp = new java.sql.Date(revisionEntity.getRevtstmp());
            dto.setDate(String.valueOf(timestamp));
            dto.setItemTypeId(String.valueOf(entity.getId()));
            dto.setItemTypeName(entity.getName());
            dto.setUsername(revisionEntity.getUsername());
            resultsToString.add(dto);
        }
        return resultsToString;
    }

    public void softDeleteProduct(Long id) {
        Product product = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setDeleted(true);
        repo.save(product);
    }

    public List<Product> findAllActiveProduct() {
        List<Product> active = repo.findAllActive();
        return active;
    }

    public List<Product> getSoftDeletesProduct() {
        List<Product> deletedFields = repo.findSoftDeletes();
        return deletedFields;
    }

    public void restoreRecordProduct(Long id) {
        Product product = repo.findById(id).get();
        product.setDeleted(false);
        repo.save(product);
    }

    public List<ProductAuditDTO> getAllAuditsProduct() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(Product.class, false, true)
                .addProjection(AuditEntity.revisionNumber())
                .addProjection(AuditEntity.revisionType())
                .addProjection(AuditEntity.revisionProperty("revtstmp"))
                .addProjection(AuditEntity.property("id"))
                .addProjection(AuditEntity.property("name"))
                .addProjection(AuditEntity.revisionProperty("username"));

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        List<ProductAuditDTO> resultsToString = new ArrayList<>();

        for (Object[] row : results) {
            ProductAuditDTO dto = new ProductAuditDTO();
            dto.setRevId(String.valueOf(row[0]));
            dto.setAction(String.valueOf(row[1]));
            long x = (long) row[2];
            java.sql.Date timestamp = new java.sql.Date(x);
            dto.setDate(String.valueOf(timestamp));
            dto.setProductId(String.valueOf(row[3]));
            dto.setProductName(String.valueOf(row[4]));
            dto.setUsername(String.valueOf(row[5]));
            resultsToString.add(dto);
        }

        return resultsToString;
    }

    public int activeProductsCount() {
        List<Product> products = repo.findAllActive();

        int count = products.size();

        return count;
    }

}


