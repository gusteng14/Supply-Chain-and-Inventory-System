package com.barkstore.Barkstore.requisition;

import com.barkstore.Barkstore.products.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class RequestDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Integer quantity;
    private Integer total;
    private String product;
    private String status = "Pending";

    @Transient
    private String imgUrl;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String lastUpdatedBy;

//    @OneToOne
//    private Product product;

    @ManyToOne
    @JoinColumn(name = "header_id", nullable = false, referencedColumnName = "id")
    private RequestHeader headerId;

    //    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;
//    private ItemType itemType;
//    NEED TO IMPLEMENT SOFT DELETE FUNCTION
}
