package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bgms")
@ToString()
public class bgms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bgm_id", nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String artist;
    @Column(nullable = false)
    private String title;
    @Column(name = "created_at", nullable = false)
    private String createdAt;
}
