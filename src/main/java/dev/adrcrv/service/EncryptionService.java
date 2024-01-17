package dev.adrcrv.service;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import dev.adrcrv.dto.KeyPairDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EncryptionService {
    private static final String RSA = "RSA";
    private static final String AES = "AES";
    private static final String FIXED_SALT = "a1a2b88c-f695-42a4-b350-53a391ccddea";
    private static final String PBKDF2_HMAC_SHA256 = "PBKDF2WithHmacSHA256";
    private static final Integer INTERACTIONS = 65536;

    public KeyPairDTO generateKeyPair(Integer keySize) throws Exception {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(RSA);
        keyGenerator.initialize(keySize);

        KeyPair keyPair = keyGenerator.genKeyPair();
        byte[] privateKey = keyPair.getPrivate().getEncoded();
        byte[] publicKey = keyPair.getPublic().getEncoded();

        String privateKeyDecoded = Base64.getEncoder().encodeToString(privateKey);
        String publicKeyDecoded = Base64.getEncoder().encodeToString(publicKey);

        KeyPairDTO keyPairDTO = new KeyPairDTO();
        keyPairDTO.setPrivateKey(privateKeyDecoded);
        keyPairDTO.setPublicKey(publicKeyDecoded);

        return keyPairDTO;
    }

    public String encrypt(String message, String publicKey) throws Exception {
        PublicKey publicKeyDecoded = restorePublicKey(publicKey);

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyDecoded);
        byte[] encoded = cipher.doFinal(message.getBytes());

        return Base64.getEncoder().encodeToString(encoded);
    }

    public String decrypt(String data, String privateKey) throws Exception {
        byte[] dataDecoded = Base64.getDecoder().decode(data);
        PrivateKey privateKeyDecoded = restorePrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKeyDecoded);
        byte[] decrypted = cipher.doFinal(dataDecoded);

        return new String(decrypted);
    }

    public SecretKey generateSecretKeyFromPassword(String password, Integer keySize) throws Exception {
        char[] passwordChar = password.toCharArray();
        byte[] salt = FIXED_SALT.getBytes();

        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_HMAC_SHA256);
        PBEKeySpec keySpec = new PBEKeySpec(passwordChar, salt, INTERACTIONS, keySize / 8);
        SecretKey secretKey = factory.generateSecret(keySpec);

        return new SecretKeySpec(secretKey.getEncoded(), AES);
    }

    public String encryptKey(String privateKey, String password, Integer keySize) throws Exception {
        EncryptionService encryptionService = new EncryptionService();
        SecretKey secretKey = encryptionService.generateSecretKeyFromPassword(password, keySize);
        PrivateKey privateKeyDecoded = restorePrivateKey(privateKey);

        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encoded = cipher.doFinal(privateKeyDecoded.getEncoded());
        return Base64.getEncoder().encodeToString(encoded);
    }

    public String decryptKey(String privateKey, String password, Integer keySize) throws Exception {
        try {
            byte[] privateKeyDecoded = Base64.getDecoder().decode(privateKey);
            SecretKey secretKey = generateSecretKeyFromPassword(password, keySize);

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] privateKeyDecrypted = cipher.doFinal(privateKeyDecoded);

            return Base64.getEncoder().encodeToString(privateKeyDecrypted);
        } catch (BadPaddingException exception) {
            return null;
        }
    }

    private PrivateKey restorePrivateKey(String privateKey) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey restorePublicKey(String publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }
}
