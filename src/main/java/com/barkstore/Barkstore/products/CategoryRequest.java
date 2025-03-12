package com.barkstore.Barkstore.products;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class CategoryRequest {
    private Long id;
    //private String categoryCode;
    private String name;
    private String description;
}
