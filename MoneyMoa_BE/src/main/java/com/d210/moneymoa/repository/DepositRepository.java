package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    Optional<Deposit> findByProductCode(String productCode);

    Optional<Deposit> findByBankCode(String bankCode);
}
