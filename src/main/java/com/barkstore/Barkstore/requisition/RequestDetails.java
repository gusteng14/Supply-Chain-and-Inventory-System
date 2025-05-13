package com.barkstore.Barkstore.requisition;

import com.barkstore.Barkstore.products.Product;
import jakarta.persistence.*;
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
public class RequestDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Integer quantity;
    private Boolean status;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @OneToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "headerId", nullable = false, referencedColumnName = "id")
    private RequestHeader headerId;

    //    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;
//    private ItemType itemType;
//    NEED TO IMPLEMENT SOFT DELETE FUNCTION
}
