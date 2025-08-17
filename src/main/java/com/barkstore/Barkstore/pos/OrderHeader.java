package com.barkstore.Barkstore.pos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderHeader {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String orderNo;

    private float subtotal;
    private float etc;
    private float total;
    public boolean voided = false;


    @CreationTimestamp
    private LocalDate createdOn;

    @UpdateTimestamp
    private LocalDate lastUpdatedOn;

    @CreationTimestamp
    @Temporal(TemporalType.TIME)
    private Date createdAt;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String lastUpdatedBy;

    @JsonIgnore
    @OneToMany(mappedBy = "headerId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails;

    @PrePersist
    public void generateOrderNo() {
        this.orderNo = String.format("TRNSCTN-%05d", id);
    }

}
