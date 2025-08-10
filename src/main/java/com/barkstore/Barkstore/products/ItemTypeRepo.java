package com.barkstore.Barkstore.products;

import com.barkstore.Barkstore.Supplier.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemTypeRepo extends RevisionRepository<ItemType, Long, Long>, JpaRepository<ItemType, Long> {
    Optional<ItemType> findByName(String name);
    @Query("SELECT i FROM ItemType i where i.deleted = true")
    List<ItemType> findSoftDeletes();

    @Query("SELECT i FROM ItemType i where i.deleted = false")
    List<ItemType> findAllActive();
}
