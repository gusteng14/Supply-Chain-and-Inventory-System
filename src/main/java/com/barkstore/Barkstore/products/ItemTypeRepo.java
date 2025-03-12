package com.barkstore.Barkstore.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemTypeRepo extends JpaRepository<ItemType, Long> {
    Optional<ItemType> findByName(String name);
}
