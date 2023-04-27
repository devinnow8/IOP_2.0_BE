package com.nis.exception;

public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException(String errorMsg){
        super(errorMsg);
    }
}
