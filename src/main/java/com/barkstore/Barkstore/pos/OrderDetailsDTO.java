package com.barkstore.Barkstore.pos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class OrderDetailsDTO {
    private Long id;
    private String itemName;
    private int quantity;
    private float total;
}
