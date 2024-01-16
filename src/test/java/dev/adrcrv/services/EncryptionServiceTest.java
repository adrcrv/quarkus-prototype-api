package dev.adrcrv.services;

import java.security.KeyPair;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class EncryptionServiceTest {
    @Inject
    EncryptionService encryptionService;

    @Test
    void expectGenerateKeyPairToMatchValue() throws Exception {
        Integer keySize = 1024;
        KeyPair keyPair = encryptionService.generateKeyPair(keySize);
        byte[] publicKeyEncoded = keyPair.getPublic().getEncoded();
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKeyEncoded);
        System.out.println(publicKeyBase64);
    }
}