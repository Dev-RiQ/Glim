package com.glim.payment.repository;

import com.glim.payment.domain.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    Optional<Billing> findByCustomerUid(String customerUid);
}
