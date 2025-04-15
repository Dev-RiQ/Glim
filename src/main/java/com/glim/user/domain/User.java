package com.glim.user.domain;

import com.glim.user.dto.request.UpdateDummyRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@ToString()
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String username;
    @Column(length = 100, nullable = false)
    private String password;
    @Column(length = 12, nullable = false)
    private String name;
    @Column(length = 12, nullable = false, unique = true)
    private String nickname;
    @Column(length = 2, nullable = false)
    private String sex;
    @Column(length = 6, nullable = false)
    private String birth;
    @Column(length = 100, nullable = false)
    private String content;
    @Column(length = 100, nullable = false)
    private String img;
    @Column(length = 13, nullable = false)
    private String phone;
    @Column(length = 20, nullable = false)
    private LocalDateTime created_at;
    private Long followers;
    private Long followings;
    private Long read_board_id;
    @Column(length = 12, nullable = false)
    private Role role;
    @Column(nullable = false)
    private String tags;
    @Column(length = 12, nullable = false)
    private Integer rate;

    @Builder
    public User(Long id) {
        this.id = id;
    }

    public void update(UpdateDummyRequest dummy) {
        this.id = dummy.getId();
    }
}
