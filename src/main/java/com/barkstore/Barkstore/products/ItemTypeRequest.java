package com.barkstore.Barkstore.products;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class ItemTypeRequest {
    private Long id;
    private String name;

}
