package com.barkstore.Barkstore.requisition;

import com.barkstore.Barkstore.products.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeaderRepository extends JpaRepository<RequestHeader, Long> {
    @Query("SELECT h FROM RequestHeader h where h.deleted = true")
    List<RequestHeader> findSoftDeletes();

    @Query("SELECT h FROM RequestHeader h where h.deleted = false")
    List<RequestHeader> findAllActive();
}
