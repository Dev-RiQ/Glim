package com.glim.user.domain;

import com.glim.user.dto.request.UpdateDummyRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "dummies")
@ToString()
public class Dummy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dummy_id")
    private Long id;

    @Builder
    public Dummy(Long id) {
        this.id = id;
    }

    public void update(UpdateDummyRequest dummy) {
        this.id = dummy.getId();
    }
}
