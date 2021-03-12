package com.codessquad.qna.exception;

public class DoNotAccessException extends RuntimeException {

    public DoNotAccessException() {
        super("사용자에게 접근 권한이 없습니다.");
    }
}
