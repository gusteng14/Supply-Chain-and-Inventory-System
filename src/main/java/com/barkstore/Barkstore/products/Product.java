package com.barkstore.Barkstore.products;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer itemNo;
    private String name;
    private String description;
    private int totalQuantitySold = 0;
    private Boolean isLowStock;
    private Integer reorderPoint;
    private Integer defaultRestockQuantity;
    private boolean deleted = false;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedOn;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastUpdatedBy;

//  TODO: NEED TO IMPLEMENT SOFT DELETE FUNCTION

    @Column(scale = 2)
    private BigDecimal cost;
    private Integer stock;

    @NotAudited
    @Lob
    private String imageData;

    @ManyToOne
    @JoinColumn(name="item_type_id" ,nullable = false)
    private ItemType itemType;

    @ManyToOne
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

//    public String getPhotosPath() {
//        if(id == null || photo == null) {
//            return "images/default-thumbnail.jpg";
//        }
//        return "product-photos/"+this.id+"/"+this.photo;
//    }
}
