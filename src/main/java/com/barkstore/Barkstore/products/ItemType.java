package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.appuser.MyUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ItemType implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    //private String description;
    //    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;

    // NEED TO IMPLEMENT SOFT DELETE FUNCTION

    @OneToMany(mappedBy="itemType")
    private Set<Product> products;

//    @CreationTimestamp
//    private Instant createdOn;
//
//    @UpdateTimestamp
//    private Instant lastUpdatedOn;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedOn;
}
