package me.adrigamer2950.accountguard.common.totp;

import dev.samstevens.totp.secret.SecretGenerator;

import java.security.SecureRandom;
import java.util.Base64;

public class AGSecretGenerator implements SecretGenerator {

    private final SecureRandom randomBytes = new SecureRandom();
    private final static Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public String generate() {
        return encoder.encodeToString(getRandomBytes());
    }

    private byte[] getRandomBytes() {
        int numCharacters = 48;
        byte[] bytes = new byte[(numCharacters * 5) / 8];
        randomBytes.nextBytes(bytes);

        return bytes;
    }
}
