package com.cm39.cm39.exception.user;

public enum UserExceptionMessage {
    EXISTING_ACCOUNT("이미 존재하는 계정입니다."),
    EMPTY_REQUIRED_FIELD("필수 정보가 비어있습니다."),
    INVALID_DATE_FORMAT("유효하지 않은 날짜 형식입니다."),
    INVALID_INPUT_FORMAT("유효하지 않은 입력 형식입니다."),
    WEAK_PASSWORD("비밀번호가 보안에 취약합니다."),
    INVALID_ACCOUNT("유효하지 않은 계정입니다."),
    ACCOUNT_NOT_FOUND("회원 정보를 찾을 수 없습니다."),
    FAIL_SEND_MAIL("메일 발송이 실패했습니다. 고객센터에 문의해 주세요."),
    INVALID_VERIFY_CODE("이메일 인증에 실패했습니다. 다시 시도해주세요."),
    INVALID_VERIFY_TOKEN("유효하지 않은 토큰입니다."),
    FAIL_SEND_CODE("인증 메일 전송에 실패했습니다. 다시 시도해주세요.");

    private final String message;

    UserExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
