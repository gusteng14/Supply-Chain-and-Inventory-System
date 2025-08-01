package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.appuser.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    long countByStockLessThan(int qty);
    List<Product> findTop5ByOrderByTotalQuantitySoldDesc();
    List<Product> findByCreatedOnBetween(Date startDate, Date endDate);

}
