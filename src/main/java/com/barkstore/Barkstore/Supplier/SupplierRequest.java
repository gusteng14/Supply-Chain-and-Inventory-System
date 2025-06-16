package com.barkstore.Barkstore.Supplier;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class SupplierRequest {
    private Long id;
    private String supplierNo;
    @NotBlank
    @NotNull
    private String name;
    @NotBlank
    @NotNull
    private String addressLine1;
    @NotBlank
    @NotNull
    private String addressLine2;
    @NotBlank
    @NotNull
    private String addressLine3;
    @NotBlank
    @NotNull
    private String contactNo;
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
    private String agentName;
    @NotBlank
    @NotNull
    private String agentContactNo;

    //    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;
//    private ItemType itemType;
//    NEED TO IMPLEMENT SOFT DELETE FUNCTION

}
