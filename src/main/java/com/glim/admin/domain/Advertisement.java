package com.glim.admin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "advertisement")
@ToString()
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id", nullable = false)
    private Long id;
    @Column(name = "board_id", nullable = false)
    private Long boardId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdvertisementStatus status;
    @Column(nullable = false)
    private String rejectionReason;

    public Advertisement(Long boardId) {
        this.boardId = boardId;
        this.status = AdvertisementStatus.PENDING;
        this.rejectionReason = "";
    }

}
