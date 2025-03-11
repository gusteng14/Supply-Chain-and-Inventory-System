package com.barkstore.Barkstore.appuser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByEmail(String email);
    //Optional<MyUser> findByUsername(String username);
    MyUser findByUsername (String username);
    Boolean existsByUsername(String username);


//    @Transactional
//    @Modifying
//    @Query("UPDATE MyUser a " +
//            "SET a.enabled = TRUE WHERE a.email = ?1")
//    int enableAppUser(String email);
}
