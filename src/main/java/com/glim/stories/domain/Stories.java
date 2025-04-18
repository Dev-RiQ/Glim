package com.glim.stories.domain;

import com.glim.stories.dto.request.UpdateStoryRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Stories")
@ToString()
public class Stories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id", nullable = false)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private Integer likes;
    @Column(nullable = false)
    private Integer views;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Stories(Long userId ,String fileName, Integer likes, Integer views, LocalDateTime createdAt) {
        this.userId = userId;
        this.fileName = fileName;
        this.likes = likes;
        this.views = views;
        this.createdAt = createdAt;
    }

    public void update(UpdateStoryRequest request) {
//        this.likes = request.;
    }
}
