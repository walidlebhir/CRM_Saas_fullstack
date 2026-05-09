package com.oualid.CRMSaas.Services;

import com.oualid.CRMSaas.Tools.Email_Commande.Command;
import org.springframework.stereotype.Service;

@Service
public class CommandInvoker {
    public void execute(Command command) {
        command.execute();
    }
}
