package com.example.clientapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto<T> {
    private String message;
    private Boolean isSuccess;
    private T data;

    public static <T> ResponseDto<T> ok(T data) {
        return ResponseDto.<T>builder()
                .data(data)
                .isSuccess(true)
                .build();
    }

    public static <T> ResponseDto<T> error(String message) {
        return ResponseDto.<T>builder()
                .isSuccess(false)
                .message(message)
                .build();
    }
}
