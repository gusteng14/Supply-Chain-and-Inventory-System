package com.barkstore.Barkstore.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

public interface SupplierRepository extends RevisionRepository<Supplier, Long, Long>,JpaRepository<Supplier, Long> {
    @Query("SELECT e FROM Supplier e where e.deleted = true")
    List<Supplier> findSoftDeletes();

    @Query("SELECT e FROM Supplier e where e.deleted =false")
    List<Supplier> findAllActive();
}
