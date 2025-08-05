package com.barkstore.Barkstore.products;

//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class ProductRequest {
    private Long id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Integer stock;
    private String category;
    private String itemType;
    private Integer reorderPoint;
    private Integer defaultRestockQuantity;
    private Boolean isLowStock;
//    private String photo;

    private String imageData;

}
