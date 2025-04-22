package com.glim.verification.repository;

import com.glim.verification.domain.AuthCodeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AuthCodeRepository extends MongoRepository<AuthCodeDocument, String> {
    boolean existsByPhoneAndCode(String phone, String code);
}