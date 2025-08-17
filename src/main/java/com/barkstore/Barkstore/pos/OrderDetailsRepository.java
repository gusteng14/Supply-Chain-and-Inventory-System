package com.barkstore.Barkstore.pos;

import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByHeaderId_Id(Long id);
    List<OrderDetail> findByCreatedOnBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(o.quantity) FROM OrderDetail o WHERE o.voided = false AND o.itemName = :itemName AND o.createdOn BETWEEN :date1 AND :date2 ORDER BY o.quantity DESC")
    Integer totalQuantitySold(String itemName, LocalDate date1, LocalDate date2);

    @Query("SELECT o FROM OrderDetail o WHERE o.voided = false")
    List<OrderDetail> findAllValid();

}
