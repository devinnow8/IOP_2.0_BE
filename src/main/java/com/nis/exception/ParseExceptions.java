package com.nis.exception;

import org.springframework.stereotype.Component;

@Component
public class ParseExceptions {

    public String parseDuplicateKeyException(String message){
        int index=message.lastIndexOf("Detail:");
        return message.substring(index+7,message.length());
    }

    public String parseJsonMappingException(String message){
        int startIndex=message.lastIndexOf("[");
        int endIndex=message.lastIndexOf("]");
        return "Data received in"+ message.substring(startIndex+1,endIndex-1)+"\" key is not in correct format";
    }
}
