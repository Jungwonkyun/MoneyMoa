package com.d210.moneymoa.repository;


import com.d210.moneymoa.dto.Saving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
    Optional<Saving> findByProductCode(String productCode);
}
