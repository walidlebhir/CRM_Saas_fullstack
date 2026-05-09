package com.oualid.CRMSaas.Exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public  EmailAlreadyExistsException(String message){
        super(message);
    }
}
