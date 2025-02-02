package com.example.demo.Common;

public class ChatNotFoundException extends RuntimeException{
    public ChatNotFoundException (String message){
        super(message);
    }
}
