package com.barkstore.Barkstore.requisition;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
public class RequestHeader {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Integer reqNo;

    private String name;
    private String description;
    private String status = "Pending";
    private Integer noOfApprovedItems;
    private Integer totalRequestedItems;
    private boolean deleted = false;

    @CreationTimestamp
    private LocalDate createdOn;

    @UpdateTimestamp
    private LocalDate lastUpdatedOn;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String lastUpdatedBy;

    @NotAudited
    @OneToMany(mappedBy = "headerId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RequestDetails> requestDetails;

    //    @AUDITING
//    private MyUser createdBy;
//    @AUDITING
//    private MyUser lastUpdatedBy;
//    private ItemType itemType;
//    NEED TO IMPLEMENT SOFT DELETE FUNCTION

    public void addDtl(RequestDetails dtl) {
        dtl.setHeaderId(this);
        requestDetails.add(dtl);
    }

    public void removeDtl(RequestDetails dtl) {
        requestDetails.remove(dtl);
        dtl.setHeaderId(null);
    }
}
