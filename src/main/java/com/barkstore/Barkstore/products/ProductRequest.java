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

    public Integer getStock() {
        return stock;
    }

    public void setStock() {
        this.stock = stock;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost() {
        this.cost = cost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
