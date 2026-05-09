package com.oualid.CRMSaas.Tools.PasswordGenerate_Builder;

import java.security.SecureRandom;

public class PasswordBuilder {

    private int length = 8;
    private boolean useSymbols = false;
    private boolean useNumbers = false;
    private boolean useUppercase = false;

    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()_+-=";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // 🔹 setter fluent (Builder pattern)

    public PasswordBuilder length(int length) {
        this.length = length;
        return this;
    }

    public PasswordBuilder withSymbols(boolean useSymbols) {
        this.useSymbols = useSymbols;
        return this;
    }

    public PasswordBuilder withNumbers(boolean useNumbers) {
        this.useNumbers = useNumbers;
        return this;
    }

    public PasswordBuilder withUppercase(boolean useUppercase) {
        this.useUppercase = useUppercase;
        return this;
    }

    //  build password
    public String build() {

        String chars = LOWER;

        if (useNumbers) {
            chars += NUMBERS;
        }

        if (useSymbols) {
            chars += SYMBOLS;
        }

        if (useUppercase) {
            chars += UPPER;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}
