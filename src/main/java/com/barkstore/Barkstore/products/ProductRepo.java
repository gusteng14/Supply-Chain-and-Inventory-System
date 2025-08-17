package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.Supplier.Supplier;
import com.barkstore.Barkstore.appuser.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProductRepo extends RevisionRepository<Product, Long, Long>, JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    long countByStockLessThan(int qty);
    List<Product> findTop5ByOrderByTotalQuantitySoldDesc();
    List<Product> findByCreatedOnBetween(Date startDate, Date endDate);
    List<Product> findByStockLessThan(int qty);

    @Query("SELECT p FROM Product p where p.deleted = true")
    List<Product> findSoftDeletes();

    @Query("SELECT p FROM Product p where p.deleted = false")
    List<Product> findAllActive();

    List<Product> findByItemType_Id(Long id);
    List<Product> findByCategory_Id(Long id);

}
