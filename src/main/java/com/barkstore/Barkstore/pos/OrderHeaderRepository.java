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
    List<OrderHeader> findByCreatedOn(Date date);
    List<OrderHeader> findByCreatedOnBetween(LocalDate startDate, LocalDate endDate);
}
