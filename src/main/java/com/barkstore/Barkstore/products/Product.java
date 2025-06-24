package com.barkstore.Barkstore.products;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Integer itemNo;
    private String name;
    private String description;
    private int totalQuantitySold = 0;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedOn;


//    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;
//    private ItemType itemType;
//    NEED TO IMPLEMENT SOFT DELETE FUNCTION

    @Column(scale = 2)
    private BigDecimal cost;
    private Integer stock;

    @Lob
    private String imageData;
//    private String imageName;
//    private String imageType;

//    private String photo;

//    @Transient
//    private String photosPath;


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
