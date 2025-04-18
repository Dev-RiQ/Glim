package com.glim.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    DUMMY_BAD_REQUEST(BAD_REQUEST,"잘못된 더미 값이 존재합니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    /* 403 FORBIDDEN : 접근 권한 제한 */
    VALID_USER_ID(FORBIDDEN, "해당 정보에 접근 권한이 존재하지 않습니다."),

    /* 404 NOT_FOUND : 해당 정보 존재하지 않음 */
    DUMMY_NOT_FOUND(NOT_FOUND, "해당 더미 정보를 찾을 수 없습니다."),
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),

    /* 409 CONFLICT : 중복 데이터 존재 */
    DUPLICATE_USER(CONFLICT, "이미 존재하는 사용자입니다"),
    DUPLICATE_USERNAME(CONFLICT, "이미 사용 중인 아이디입니다."),
    DUPLICATE_NICKNAME(CONFLICT, "이미 사용 중인 닉네임입니다."),
    DUPLICATE_PHONE(CONFLICT, "이미 등록된 전화번호입니다."),

    /* 500 : */
    UNSUCCESSFUL_DUMMY_INSERT(INTERNAL_SERVER_ERROR,"더미 업로드에 실패했습니다."),
    UNSUCCESSFUL_DUMMY_UPDATE(INTERNAL_SERVER_ERROR,"해당 정보의 상태 변경에 실패했습니다."),

    /* 탈퇴회원 */
    ALREADY_DELETED_USER(CONFLICT, "이미 탈퇴한 회원입니다.");


    private final HttpStatus httpStatus;
    private final String detail;

    public static CustomException throwDummyNotFound(){
        throw new CustomException((DUMMY_NOT_FOUND));
    }

    public List<String> getDetail() {
        return List.of(this.detail);
    }

    public static CustomException throwUserNotFound(){
        throw new CustomException((USER_NOT_FOUND));
    }

    public static CustomException throwInvalidPassword(){
        throw new CustomException((INVALID_PASSWORD));
    }

}
