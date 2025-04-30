package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "bgms")
@ToString()
public class Bgms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bgm_id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String artist;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String fileName;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Bgms(String artist, String title, String fileName, LocalDateTime createdAt) {
        this.artist = artist;
        this.title = title;
        this.fileName = fileName;
        this.createdAt = createdAt;
    }
}
