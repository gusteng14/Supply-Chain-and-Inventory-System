package com.barkstore.Barkstore.Supplier;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Supplier {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String supplierNo;
    private String name;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String contactNo;
    private String agentName;
    private String agentContactNo;

    @CreationTimestamp
    private Instant createdOn;
    @UpdateTimestamp
    private Instant lastUpdatedOn;

    public String getAddress() {
        return addressLine1 + " " + addressLine2 + " " + addressLine3;
    }


    //    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;
//    private ItemType itemType;
//    NEED TO IMPLEMENT SOFT DELETE FUNCTION

}
