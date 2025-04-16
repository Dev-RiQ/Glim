package com.glim.borad.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "board_tags")
@ToString()
public class BoardTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_tag_id", nullable = false)
    private Long id;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Column(nullable = false)
    private String tag;
}
