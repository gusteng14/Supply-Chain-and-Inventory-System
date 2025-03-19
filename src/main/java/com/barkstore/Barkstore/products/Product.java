package com.barkstore.Barkstore.products;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product implements Serializable {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;

    private Integer itemNo;
    private String name;
    private String description;


    //    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;
    // private ItemType itemType;
    // NEED TO IMPLEMENT SOFT DELETE FUNCTION

    private Integer cost;
    private Integer stock;

//    @Lob
//    private byte[] imageData;
//    private String imageName;
//    private String imageType;

    private String photo;

    @Transient
    private String photosPath;


    @ManyToOne
    private ItemType itemType;

    @ManyToOne
    private Category category;

    public String getPhotosPath() {
        if(id == null || photo == null) {
            return "images/default-thumbnail.jpg";
        }
        return "product-photos/"+this.id+"/"+this.photo;
    }






}
