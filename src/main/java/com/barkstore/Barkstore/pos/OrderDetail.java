package com.barkstore.Barkstore.pos;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String itemName;
    private int quantity;
    private float unitPrice;
    private float total;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant lastUpdatedOn;

    @ManyToOne
    @JoinColumn(name = "header_id", nullable = false, referencedColumnName = "id")
    private OrderHeader headerId;

}
