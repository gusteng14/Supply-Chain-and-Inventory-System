package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.appuser.MyUser;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
//@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String categoryCode;
    private String name;
    private String description;
    private boolean deleted = false;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastUpdatedBy;

    // TODO: NEED TO IMPLEMENT SOFT DELETE FUNCTION

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

}
