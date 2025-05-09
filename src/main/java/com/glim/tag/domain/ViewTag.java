package com.glim.tag.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "view_tags")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long viewTagId;

    @Column(nullable = false)
    private Long userId;

    private String tag;

    private Integer views;

    public void increaseViews() {
        this.views++;
    }
}
