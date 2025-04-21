package com.glim.verification.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "auth_codes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCodeDocument {

    @Id
    private String phone;

    private String code;

//    @TimeToLive
//    private long ttl = 180;
    private Instant expiresAt;
}