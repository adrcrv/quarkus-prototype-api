package dev.adrcrv.services;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncryptionService {
    private final String rsa = "RSA";
    private final String aes = "AES";
    private final String tempSalt = "a1a2b88c-f695-42a4-b350-53a391ccddea";
    private final String pbkdf2HmacSha256 = "PBKDF2WithHmacSHA256";
    private final Integer interactions = 65536;

    public KeyPair generateKeyPair(Integer keySize) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(this.rsa);
        keyGenerator.initialize(keySize);
        return keyGenerator.genKeyPair();
    }

    public byte[] encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(this.rsa);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message.getBytes());
    }

    public String decrypt(byte[] data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(this.rsa);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(data);
        return new String(decryptedData);
    }

    public SecretKey generateSecretKeyFromPassword(String password, Integer keySize) throws Exception {
        char[] passwordChar = password.toCharArray();
        byte[] salt = this.tempSalt.getBytes();

        SecretKeyFactory factory = SecretKeyFactory.getInstance(this.pbkdf2HmacSha256);
        PBEKeySpec keySpec = new PBEKeySpec(passwordChar, salt, this.interactions, keySize / 8);
        SecretKey secretKey = factory.generateSecret(keySpec);
        return new SecretKeySpec(secretKey.getEncoded(), this.aes);
    }

    public byte[] encryptKey(PrivateKey privateKey, String password, Integer keySize) throws Exception {
        Cipher cipher = Cipher.getInstance(this.aes);
        EncryptionService encryptionService = new EncryptionService();
        SecretKey secretKey = encryptionService.generateSecretKeyFromPassword(password, keySize);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(privateKey.getEncoded());
    }

    public String decryptKey(byte[] data, String password, Integer keySize) throws Exception {
        Cipher cipher = Cipher.getInstance(this.aes);
        SecretKey secretKey = generateSecretKeyFromPassword(password, keySize);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(data));
    }
}
