package dev.adrcrv.services;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncryptionService {
    private final String algorithm = "RSA";

    public KeyPair generateKeyPair(Integer keySize) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(algorithm);
        keyGenerator.initialize(keySize);
        return keyGenerator.genKeyPair();
    }

    public byte[] encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    public String decrypt(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(data);
        return new String(decryptedData);
    }
}
