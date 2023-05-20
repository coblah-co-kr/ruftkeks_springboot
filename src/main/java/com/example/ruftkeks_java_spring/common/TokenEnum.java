package com.example.ruftkeks_java_spring.common;

public enum TokenEnum {
    OK(true, "유효한 토큰입니다."),
    WRONG(false, "잘못된 JWT 서명입니다."),
    EXPIRED(false, "만료된 JWT 토큰입니다."),
    NOT_SUPPORT(false, "지원되지않는 JWT 토큰입니다."),
    ILLEGAL(false, "JWT 토큰이 잘못되었습니다.");

    public Boolean caseBool;
    public String text;

    TokenEnum(Boolean caseBool, String text) {
        this.caseBool = caseBool;
        this.text = text;
    }
}
