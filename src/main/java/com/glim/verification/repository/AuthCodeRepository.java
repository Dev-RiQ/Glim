package com.glim.verification.repository;

import com.glim.verification.domain.AuthCodeDocument;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthCodeRepository extends MongoRepository<AuthCodeDocument,String> {
    Optional<AuthCodeDocument> findByPhone(String phone);
}
