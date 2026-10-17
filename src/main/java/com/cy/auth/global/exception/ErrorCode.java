package com.cy.auth.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    VALID_EXCEPTION(HttpStatus.BAD_REQUEST, "요청값이 올바르지 않습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    OAUTH_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"제공자로부터 이메일 정보를 받지 못했습니다.")
,
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용중인 이메일 입니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용중인 닉네임 입니다."),

    USERNAME_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "닉네임 생성에 실패하였습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
