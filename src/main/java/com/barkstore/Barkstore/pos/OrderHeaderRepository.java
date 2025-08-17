package com.barkstore.Barkstore.pos;

import com.barkstore.Barkstore.products.Product;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
    List<OrderHeader> findByCreatedOn(LocalDate date);
    List<OrderHeader> findByCreatedOnBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT o FROM OrderHeader o WHERE o.createdOn = :date AND o.voided = false")
    List<OrderHeader> findValidCreatedOn(LocalDate date);

    @Query("SELECT o FROM OrderHeader o WHERE o.createdOn BETWEEN :startDate AND :endDate AND o.voided = false")
    List<OrderHeader> findValidCreatedOnBetween(LocalDate startDate, LocalDate endDate);
}
