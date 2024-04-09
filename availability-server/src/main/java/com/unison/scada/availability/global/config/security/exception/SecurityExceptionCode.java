package com.unison.scada.availability.global.config.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SecurityExceptionCode {
    AUTHENTICATION_ENTRY_POINT(1001, "인증 실패"),
    BAD_CREDENTIALS(1002, "비밀번호 불일치"),
    DEFAULT(1999, "인증/인가 예외");

    private final int code;
    private final String message;

}
