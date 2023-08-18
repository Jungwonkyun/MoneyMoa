package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.Cma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmaRepository extends JpaRepository<Cma, Long>{
}
