package com.barkstore.Barkstore.Supplier;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Optional;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
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
    private boolean deleted = false;

    @NotAudited
    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
//    @Column(insertable = false)
    private String lastUpdatedBy;

    public String getAddress() {
        return addressLine1 + " " + addressLine2 + " " + addressLine3;
    }

}
