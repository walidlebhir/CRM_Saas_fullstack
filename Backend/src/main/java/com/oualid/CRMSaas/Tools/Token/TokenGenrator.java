package com.oualid.CRMSaas.Tools.Token;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenrator {

    public static String  tokengerator(){
        SecureRandom random = new SecureRandom() ;
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
