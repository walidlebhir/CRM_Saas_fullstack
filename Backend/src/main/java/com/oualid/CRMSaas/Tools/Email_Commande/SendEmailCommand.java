package com.oualid.CRMSaas.Tools.Email_Commande;

import com.oualid.CRMSaas.Services.EmailService;

public class SendEmailCommand implements Command {

    private final EmailService emailService;
    private final String to;
    private final String subject;
    private final String body;

    public SendEmailCommand(EmailService emailService,
                            String to,
                            String subject,
                            String body) {
        this.emailService = emailService;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }


    @Override
    public void execute() {
        emailService.sendEmail(to, subject, body);
    }
}
