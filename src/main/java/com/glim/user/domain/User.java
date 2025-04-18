package com.glim.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
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
    @Column(nullable = false, unique = true)
    private String username; // 로그인 할때 id
    @Column(nullable = false)
    private String password; // 로그인 시 password
    @Column(nullable = false)
    private String name; // 유저 이름 (닉네임과 차별됨)
    @Column(nullable = true, unique = true)
    private String nickname; // @knk_0611
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private Sex sex;
    @Column(nullable = false)
    private LocalDate birth;
    @Column(nullable = false)
    private String content;     // 인스타 프로필 밑에 쓰는 유튜브 링크나 다른 계정넣어놓거나 ,, 뭐그런
    @Column(nullable = false)
    private String img;
    @Column(nullable = false)
    private String phone;
    @Column(name = "createdDate", nullable = false)
    private LocalDateTime created_at;
    @Column(nullable = false)
    private Long followers;
    @Column(nullable = false)
    private Long followings;
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private String tags;
    @Column(nullable = false)
    private Integer rate;
    @Column(name = "read_board_id", nullable = false)
    private Long readBoardId;
    @Enumerated(EnumType.STRING)
    private PlatForm platForm;


    @Builder
    public User(Long id, String username, String password, String name, String nickname, Sex sex, LocalDate birth,
                String content, String img, String phone, LocalDateTime created_at, Long followers,
                Long followings, Role role, String tags, Integer rate, Long readBoardId, PlatForm platForm) {
        this.id = id;
        this.username = username != null ? username : "";
        this.password = password != null && !password.isEmpty()
                ? /*new UserPasswordEncoder(password).getPassword()*/password
                : "";
        this.name = name;
        this.nickname = nickname;
        this.sex = sex != null ? sex : Sex.UNKNOWN;
        this.birth = birth != null ? birth : LocalDate.of(0000, 01, 01);
        this.content = content != null ? content : "";
        this.img = img != null ? img : "";
        this.phone = phone != null ? phone : "";
        this.created_at = created_at != null ? created_at : LocalDateTime.now();
        this.followers = followers != null ? followers : 0L;
        this.followings = followings != null ? followings : 0L;
        this.role = role;
        this.tags = tags != null ? tags : "";
        this.rate = rate != null ? rate : 0;
        this.readBoardId = readBoardId != null ? readBoardId : 0L;
        this.platForm = platForm;
    }


    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }


}
