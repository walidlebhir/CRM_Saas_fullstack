package com.oualid.CRMSaas.Tools.AdapterSystem;

public class LeadAdapterFactory {
    public static  LeadAdapter getAdapter(String source ){

        return  switch(source){
            case "SYSTEM_A" -> new SystemEntreprise();
            case "SYSTEM_B" -> new SystemEntreprise() ;
            default -> throw new IllegalArgumentException("Unknown source");
        };

    }
}
