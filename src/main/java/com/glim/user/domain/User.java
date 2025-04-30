package com.glim.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 로그인 할 때 id

    @Column(nullable = false)
    private String password; // 로그인 시 password

    @Column(nullable = false)
    private String name; // 유저 이름

    @Column(nullable = true, unique = true)
    private String nickname; // @knk_0611 스타일 닉네임

    @Column(nullable = false)
    private String content; // 한 줄 소개, 링크 같은거

    @Column(nullable = false)
    private String img; // 프로필 이미지 URL

    @Column(nullable = false)
    private String phone; // 전화번호

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime created_at; // 가입 일시

    @Column(nullable = false)
    private Long followers; // 팔로워 수

    @Column(nullable = false)
    private Long followings; // 팔로잉 수

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 유저 권한

    @Column(nullable = false)
    private String tags; // 태그 목록 (CSV 형태)

    @Column(nullable = false)
    private Integer rate; // 구매등급

    @Column(name = "read_board_id", nullable = false)
    private Long readBoardId; // 마지막 읽은 게시글 ID

    @Column(name = "read_alarm_id", nullable = false)
    private Long readAlarmId; // 마지막 읽은 알림 ID

    @Enumerated(EnumType.STRING)
    private PlatForm platForm; // 로그인 플랫폼 (GOOGLE, NAVER, KAKAO)

    @Builder
    public User(Long id, String username, String password, String name, String nickname,
                String content, String img, String phone, LocalDateTime created_at,
                Long followers, Long followings, Role role, String tags,
                Integer rate, Long readBoardId, Long readAlarmId, PlatForm platForm) {

        this.id = id;
        this.username = username != null ? username : "";
        this.password = password != null && !password.isEmpty() ? password : "";
        this.name = name;
        this.nickname = nickname;
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
        this.readAlarmId = readAlarmId != null ? readAlarmId : 0L;
        this.platForm = platForm;
    }

    // ======================== 메서드 ========================

    // 비밀번호 암호화
    public void encodePassword(PasswordEncoder encoder) {
        this.password = encoder.encode(this.password);
    }

    // 비밀번호 수정
    public void updatePassword(String password) {
        this.password = password;
    }

    // 마지막 읽은 알림 ID 수정
    public void updateReadAlarmId(Long readAlarmId) {
        this.readAlarmId = readAlarmId;
    }

    // 마지막 읽은 게시글 ID 수정
    public void updateReadBoardId(Long readBoardId) {
        this.readBoardId = readBoardId;
    }

    // 소개글(content) 수정
    public void updateContent(String content) {
        this.content = content;
    }

    // 프로필 이미지(img) 수정
    public void updateImg(String img) {
        this.img = img;
    }
    // 구매등급(rate) 수정
    public void updateRate(Integer rate) {
        this.rate = rate;
    }
    // 전화번호 수정
    public void updatePhone(String phone) {
        this.phone = phone;
    }

}
