package com.example.epos.common;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.security.SecureRandom;

public class PasswordHandler {
    public static String generateStaffPassword() {
        RandomStringGenerator passwordGenerator = new RandomStringGenerator.Builder()
                .withinRange(' ', '~') // ASCII range from space to tilde
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .usingRandom(new SecureRandom()::nextInt)
                .build();

        String randomPassword = passwordGenerator.generate(12); // Generates a 12-character password
        return randomPassword;
    }
}
