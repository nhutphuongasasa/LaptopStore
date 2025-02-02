package com.example.demo.Common;

public class SaleNotFoundException extends RuntimeException{
    public SaleNotFoundException (String message){
        super(message);
    }
}
