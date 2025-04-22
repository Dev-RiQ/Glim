package com.glim.verification.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "auth_code_document")
public class AuthCodeDocument {

    @Id
    private String id;
    private String phone;
    private String code;
    private LocalDateTime createdAt;

    public AuthCodeDocument(String phone, String code, LocalDateTime createdAt) {
        this.phone = phone;
        this.code = code;
        this.createdAt = createdAt;
    }
}