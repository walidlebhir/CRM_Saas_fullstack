package com.oualid.CRMSaas.Exception;

import com.oualid.CRMSaas.Entity.Company;

public class CompanyNotFound extends  RuntimeException{
    public CompanyNotFound(String message){
        super(message);
    }
}
