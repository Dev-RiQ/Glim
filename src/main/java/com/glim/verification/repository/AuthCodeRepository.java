package com.glim.verification.repository;

import com.glim.verification.domain.AuthCodeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface AuthCodeRepository extends MongoRepository<AuthCodeDocument, String> {
    Optional<AuthCodeDocument> findByPhone(String phone); // 인증번호 저장 및 조회
    Optional<AuthCodeDocument> findTopByPhoneOrderByCreatedAtDesc(String phone); // 제일 최근에 전송된 인증번호
}