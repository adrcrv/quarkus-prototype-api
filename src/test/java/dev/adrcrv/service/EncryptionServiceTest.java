package dev.adrcrv.service;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dev.adrcrv.dto.KeyPairDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class EncryptionServiceTest {
    @Inject
    private EncryptionService encryptionService;

    private static final Integer KEY_SIZE = 1024;
    private static final String MESSAGE = "Hello World";
    private static final String PASSWORD = "hxo2O#RK9fQjX1eKG3@KyZHx";
    private static final String UUID = "3eb954bd-2f82-4f7a-b70d-65c0566470bb";

    @Test
    public void expectGenerateKeyPairsToAssertNotEquals() throws Exception {
        KeyPairDTO keyPair1 = encryptionService.generateKeyPair(KEY_SIZE);
        KeyPairDTO keyPair2 = encryptionService.generateKeyPair(KEY_SIZE);

        String privateKeyEncoded1 = keyPair1.getPrivateKey();
        String privateKeyEncoded2 = keyPair2.getPrivateKey();
        String publicKeyEncoded1 = keyPair1.getPublicKey();
        String publicKeyEncoded2 = keyPair2.getPublicKey();

        Assertions.assertNotEquals(privateKeyEncoded1, privateKeyEncoded2);
        Assertions.assertNotEquals(publicKeyEncoded1, publicKeyEncoded2);
        Assertions.assertNotEquals(privateKeyEncoded1, publicKeyEncoded1);
        Assertions.assertNotEquals(privateKeyEncoded2, publicKeyEncoded2);
    }

    @Test
    public void expectEncryptDatasToAssertNotEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String publicKey = keyPair.getPublicKey();

        String dataEncoded1 = encryptionService.encrypt(MESSAGE, publicKey);
        String dataEncoded2 = encryptionService.encrypt(MESSAGE, publicKey);

        Assertions.assertNotEquals(dataEncoded1, dataEncoded2);
    }

    @Test
    public void expectEncryptEncodedAndDecodedToAssertNotEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String publicKey = keyPair.getPublicKey();

        String dataEncoded = encryptionService.encrypt(MESSAGE, publicKey);

        Assertions.assertNotEquals(MESSAGE, dataEncoded);
    }

    @Test
    public void expectDecryptMessageAndDecodedToAssertEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String publicKey = keyPair.getPublicKey();
        String privateKey = keyPair.getPrivateKey();

        String dataEncoded = encryptionService.encrypt(MESSAGE, publicKey);
        String dataDecoded = encryptionService.decrypt(dataEncoded, privateKey);

        Assertions.assertEquals(MESSAGE, dataDecoded);
    }

    @Test
    public void expectDecryptEncodedAndDecodedToAssertNotEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String publicKey = keyPair.getPublicKey();
        String privateKey = keyPair.getPrivateKey();

        String dataEncoded = encryptionService.encrypt(MESSAGE, publicKey);
        String dataDecoded = encryptionService.decrypt(dataEncoded, privateKey);

        Assertions.assertNotEquals(dataEncoded, dataDecoded);
    }

    @Test
    public void expectGenerateSecretKeysFromDiffPasswordToAssertNotEquals() throws Exception {
        SecretKey secretKeyEncoded = encryptionService.generateSecretKeyFromPassword(PASSWORD, KEY_SIZE);
        SecretKey diffSecretKeyEncoded = encryptionService.generateSecretKeyFromPassword(UUID, KEY_SIZE);

        Assertions.assertNotEquals(secretKeyEncoded, diffSecretKeyEncoded);
    }

    @Test
    public void expectGenerateSecretKeysFromSamePasswordToAssertEquals() throws Exception {
        SecretKey secretKeyEncoded1 = encryptionService.generateSecretKeyFromPassword(PASSWORD, KEY_SIZE);
        SecretKey secretKeyEncoded2 = encryptionService.generateSecretKeyFromPassword(PASSWORD, KEY_SIZE);

        Assertions.assertEquals(secretKeyEncoded1, secretKeyEncoded2);
    }

    @Test
    public void expectEncryptKeyEncodedAndDecodedToAssertNotEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String privateKey = keyPair.getPrivateKey();
        String privateKeyEncoded = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);

        Assertions.assertNotEquals(privateKeyEncoded, privateKey);
    }

    @Test
    public void expectEncryptKeyEncodedToAssertEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String privateKey = keyPair.getPrivateKey();

        String privateKeyEncoded1 = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);
        String privateKeyEncoded2 = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);

        Assertions.assertEquals(privateKeyEncoded1, privateKeyEncoded2);
    }

    @Test
    public void expectDecryptKeyAndDecodedToAssertEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String privateKey = keyPair.getPrivateKey();

        String privateKeyEncoded = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);
        String privateKeyDecoded = encryptionService.decryptKey(privateKeyEncoded, PASSWORD, KEY_SIZE);

        Assertions.assertEquals(privateKeyDecoded, privateKey);
    }

    @Test
    public void expectDecryptKeyDecodedAndEncodedToAssertNotEquals() throws Exception {
        KeyPairDTO keyPair = encryptionService.generateKeyPair(KEY_SIZE);
        String privateKey = keyPair.getPrivateKey();

        String privateKeyEncoded = encryptionService.encryptKey(privateKey, PASSWORD, KEY_SIZE);
        String privateKeyDecoded = encryptionService.decryptKey(privateKeyEncoded, PASSWORD, KEY_SIZE);

        Assertions.assertNotEquals(privateKeyEncoded, privateKeyDecoded);
    }
}