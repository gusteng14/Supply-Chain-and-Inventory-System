package com.barkstore.Barkstore.requisition;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
@ToString
public class HeaderDTO {
    private String requestName;
    private String requestDescription;
}
