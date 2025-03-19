package com.barkstore.Barkstore.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private Integer cost;
    private Integer stock;
    private String photo;
//    private byte[] imageData;

}
