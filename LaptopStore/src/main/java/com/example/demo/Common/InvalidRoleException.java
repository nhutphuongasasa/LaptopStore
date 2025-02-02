package com.example.demo.Common;

// Custom Exception cho lỗi liên quan tới lựa chọn vai trò sai
public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String message) {
        super(message);
    }
}