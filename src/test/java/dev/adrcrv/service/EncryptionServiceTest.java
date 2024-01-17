package dev.adrcrv.service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class EncryptionServiceTest {
    @Inject
    private EncryptionService encryptionService;

    private static final Integer KEY_SIZE = 1024;
    private static final String MESSAGE = "Hello World";
    private static final String PASSWORD = "hxo2O#RK9fQjX1eKG3@KyZHx";
    private static final String UUID = "3eb954bd-2f82-4f7a-b70d-65c0566470bb";

    @Test
    void expectGenerateKeyPairToAssertConditions() throws Exception {
        KeyPair keyPair1 = encryptionService.generateKeyPair(KEY_SIZE);
        KeyPair keyPair2 = encryptionService.generateKeyPair(KEY_SIZE);

        byte[] privateKeyEncoded1 = keyPair1.getPrivate().getEncoded();
        byte[] privateKeyEncoded2 = keyPair2.getPrivate().getEncoded();
        byte[] publicKeyEncoded1 = keyPair1.getPublic().getEncoded();
        byte[] publicKeyEncoded2 = keyPair2.getPublic().getEncoded();

        String privateKeyEncodedString1 = new String(privateKeyEncoded1);
        String privateKeyEncodedString2 = new String(privateKeyEncoded2);
        String publicKeyEncodedString1 = new String(publicKeyEncoded1);
        String publicKeyEncodedString2 = new String(publicKeyEncoded2);

        Assertions.assertNotEquals(privateKeyEncodedString1, privateKeyEncodedString2);
        Assertions.assertNotEquals(publicKeyEncodedString1, publicKeyEncodedString2);
        Assertions.assertNotEquals(privateKeyEncodedString1, publicKeyEncodedString1);
        Assertions.assertNotEquals(privateKeyEncodedString2, publicKeyEncodedString2);
    }

    @Test
    void expectEncryptToAssertConditions() throws Exception {
        KeyPair keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        PublicKey publicKey = keyPair.getPublic();

        byte[] dataEncoded1 = encryptionService.encrypt(MESSAGE, publicKey);
        byte[] dataEncoded2 = encryptionService.encrypt(MESSAGE, publicKey);

        String dataEncodedString1 = new String(dataEncoded1);
        String dataEncodedString2 = new String(dataEncoded2);

        Assertions.assertNotEquals(dataEncodedString1, dataEncodedString2);
        Assertions.assertNotEquals(MESSAGE, dataEncodedString1);
    }

    @Test
    void expectDecryptToAssertConditions() throws Exception {
        KeyPair keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        byte[] dataEncoded = encryptionService.encrypt(MESSAGE, publicKey);
        String dataEncodedString = new String(dataEncoded);
        String dataDecoded = encryptionService.decrypt(dataEncoded, privateKey);

        Assertions.assertNotEquals(dataEncodedString, dataDecoded);
        Assertions.assertEquals(MESSAGE, dataDecoded);
    }

    @Test
    void expectGenerateSecretKeyFromPasswordToAssertConditions() throws Exception {
        SecretKey secretKeyEncoded1 = encryptionService.generateSecretKeyFromPassword(PASSWORD, KEY_SIZE);
        SecretKey secretKeyEncoded2 = encryptionService.generateSecretKeyFromPassword(PASSWORD, KEY_SIZE);
        SecretKey diffSecretKeyEncoded = encryptionService.generateSecretKeyFromPassword(UUID, KEY_SIZE);

        Assertions.assertEquals(secretKeyEncoded1, secretKeyEncoded2);
        Assertions.assertNotEquals(secretKeyEncoded1, diffSecretKeyEncoded);
    }

    @Test
    void expectEncryptKeyToAssertConditions() throws Exception {
        KeyPair keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyString = new String(privateKey.getEncoded());

        byte[] privateKeyEncoded1 = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);
        byte[] privateKeyEncoded2 = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);

        String privateKeyEncodedString1 = new String(privateKeyEncoded1);
        String privateKeyEncodedString2 = new String(privateKeyEncoded2);

        Assertions.assertNotEquals(privateKeyEncodedString1, privateKeyString);
        Assertions.assertEquals(privateKeyEncodedString1, privateKeyEncodedString2);
    }

    @Test
    void expectdecryptKeyToAssertConditions() throws Exception {
        KeyPair keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        PrivateKey privateKey = keyPair.getPrivate();
        String privateKeyString = new String(privateKey.getEncoded());

        byte[] privateKeyEncoded = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);
        String privateKeyDecoded = encryptionService.decryptKey(privateKeyEncoded, PASSWORD, KEY_SIZE);
        String privateKeyEncodedString = new String(privateKeyEncoded);

        Assertions.assertEquals(privateKeyDecoded, privateKeyString);
        Assertions.assertNotEquals(privateKeyEncodedString, privateKeyDecoded);
    }
}