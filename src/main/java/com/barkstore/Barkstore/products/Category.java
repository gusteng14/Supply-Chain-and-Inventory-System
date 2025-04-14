package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.appuser.MyUser;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String categoryCode;
    private String name;
    private String description;
//    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;

    // NEED TO IMPLEMENT SOFT DELETE FUNCTION

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

}
