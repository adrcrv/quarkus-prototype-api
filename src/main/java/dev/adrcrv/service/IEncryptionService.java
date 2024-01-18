package dev.adrcrv.service;

import javax.crypto.SecretKey;

import dev.adrcrv.dto.KeyPairDTO;

public interface IEncryptionService {
    KeyPairDTO generateKeyPair(Integer keySize) throws Exception;

    String encrypt(String message, String publicKey) throws Exception;

    String decrypt(String data, String privateKey) throws Exception;

    SecretKey generateSecretKeyFromPassword(String password, Integer keySize) throws Exception;

    String encryptKey(String privateKey, String password, Integer keySize) throws Exception;

    String decryptKey(String privateKey, String password, Integer keySize) throws Exception;
}
