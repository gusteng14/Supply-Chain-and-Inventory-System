package com.barkstore.Barkstore.products;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

//    private Integer itemNo;
    private String name;
    private String description;
    private Integer cost;
    private Integer stock;

    public Product(Long id, Integer itemNo, String name, String description, Integer cost, Integer stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.stock = stock;
    }

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
