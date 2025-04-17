package com.glim.user.dto.request;

import com.glim.user.domain.PlatForm;
import com.glim.user.domain.Role;
import com.glim.user.domain.Sex;
import com.glim.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddUserRequest {

    private String username;
    private String password;
    private String name;
    private String nickname;
    private Sex sex;
    private String birth;
    private String img;
    private String phone;

    // ✅ User 엔티티로 변환 (기본값도 설정)
    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password) // 비밀번호 인코딩은 서비스에서!
                .name(name)
                .nickname(nickname != null ? nickname : name)
                .sex(sex)
                .birth(birth != null ? birth : "")
                .img(img != null ? img : "")
                .phone(phone != null ? phone : "")
                .created_at(LocalDateTime.now())
                .followers(0L)
                .followings(0L)
                .role(Role.ROLE_USER)
                .tags("")
                .rate(0)
                .readBoardId(0L)
                .platForm(PlatForm.LOCAL)
                .build();
    }
}
