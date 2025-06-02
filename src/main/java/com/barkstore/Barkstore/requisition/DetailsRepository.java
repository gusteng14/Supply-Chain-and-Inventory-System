package com.barkstore.Barkstore.requisition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DetailsRepository extends JpaRepository<RequestDetails, Long> {
    List<RequestDetails> findByHeaderId_Id (Long id);
    void deleteAllByHeaderId_Id (Long id);
}
