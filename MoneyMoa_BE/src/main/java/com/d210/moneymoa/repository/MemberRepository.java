package com.d210.moneymoa.repository;

import com.d210.moneymoa.dto.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmailAndPassword(String email, String password);
    Optional<Member> findByName(String username);
}
