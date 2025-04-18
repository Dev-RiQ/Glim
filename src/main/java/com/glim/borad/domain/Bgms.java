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
    private Integer id;
    @Column(nullable = false)
    private String artist;
    @Column(nullable = false)
    private String title;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Bgms(String artist, String title, LocalDateTime createdAt) {
        this.artist = artist;
        this.title = title;
        this.createdAt = createdAt;
    }
}
