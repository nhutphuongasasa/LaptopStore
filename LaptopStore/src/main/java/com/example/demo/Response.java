package com.example.demo;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> {
    private boolean success;
    private String message;
    private T data;
}