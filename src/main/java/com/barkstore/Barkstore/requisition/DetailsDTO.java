package com.barkstore.Barkstore.requisition;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class DetailsDTO {
    private String name;
    private Integer quantity;
    private Float total;
}
