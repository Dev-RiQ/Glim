package com.glim.payment.repository;

import com.glim.payment.domain.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    boolean existsByCustomerUid(String customerUid);
    void deleteByCustomerUid(String customerUid);
    List<Billing> findAllByIsActiveTrue();
    Optional<Billing> findByCustomerUid(String customerUid);
}