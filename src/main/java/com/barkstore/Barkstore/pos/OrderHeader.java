package com.barkstore.Barkstore.pos;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class OrderHeader {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String orderNo;

    private float subtotal;
    private float etc;
    private float total;

    @CreationTimestamp
    private LocalDate createdOn;

    @UpdateTimestamp
    private LocalDate lastUpdatedOn;

    @CreationTimestamp
    @Temporal(TemporalType.TIME)
    private Date createdAt;

    @OneToMany(mappedBy = "headerId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails;

}
