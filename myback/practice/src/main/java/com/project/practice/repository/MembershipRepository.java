package com.project.practice.repository;

import com.project.practice.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByLogin_Id(Long loginId);
}
