package com.d210.moneymoa.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response {
    private boolean success; // API 요청 성공 여부를 나타내는 필드
    private int code; // API 응답 결과를 나타내는 필드
    private Result result; // API 응답 결과를 담는 필드

    // 응답 객체를 생성하는 정적 팩토리 메서드들

    // 성공적인 응답을 생성하는 메서드 (결과 데이터가 없는 경우)
    public static Response success() {
        return new Response(true, 0, null);
    }

    // 성공적인 응답을 생성하는 메서드 (결과 데이터가 있는 경우)
    public static <T> Response success(T data) {
        return new Response(true, 0, new Success<>(data));
    }

    // 실패한 응답을 생성하는 메서드
    public static Response failure(int code, String msg) {
        return new Response(false, code, new Failure(msg));
    }


}
