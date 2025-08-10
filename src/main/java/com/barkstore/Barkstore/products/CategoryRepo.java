package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.Supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends RevisionRepository<Category, Long, Long>, JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    @Query("SELECT c FROM Category c where c.deleted = true")
    List<Category> findSoftDeletes();

    @Query("SELECT c FROM Category c where c.deleted =false")
    List<Category> findAllActive();
}
