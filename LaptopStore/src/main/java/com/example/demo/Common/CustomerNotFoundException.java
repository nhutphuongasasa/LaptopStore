package com.example.demo.Common;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException (String message){
        super(message);
    }
}
