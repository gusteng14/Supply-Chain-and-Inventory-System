package com.barkstore.Barkstore.appuser;

import com.barkstore.Barkstore.products.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("SELECT r FROM Role r where r.deleted = true")
    List<Role> findSoftDeletes();

    @Query("SELECT r FROM Role r where r.deleted = false")
    List<Role> findAllActive();
}
