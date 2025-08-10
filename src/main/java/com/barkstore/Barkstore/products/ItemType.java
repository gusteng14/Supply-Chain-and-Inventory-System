package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.appuser.MyUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
//@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class ItemType implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private boolean deleted = false;
    //private String description;

    @OneToMany(mappedBy="itemType")
    private Set<Product> products;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedOn;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastUpdatedBy;
}
