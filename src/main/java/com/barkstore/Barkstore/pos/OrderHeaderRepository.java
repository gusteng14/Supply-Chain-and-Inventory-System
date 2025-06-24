package com.barkstore.Barkstore.pos;

import com.barkstore.Barkstore.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
    List<OrderHeader> findByCreatedOn(LocalDateTime date);
    List<OrderHeader> findByCreatedDate(LocalDate date);

    List<OrderHeader> findByCreatedOnBetween(LocalDateTime startDate, LocalDateTime endDate);

}
