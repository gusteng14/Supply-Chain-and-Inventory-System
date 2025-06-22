package com.barkstore.Barkstore.pos;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
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

    @CreationTimestamp
    private LocalDate createdOn;

    @UpdateTimestamp
    private LocalDate lastUpdatedOn;

    @OneToMany(mappedBy = "headerId", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderDetail> orderDetails;

}
