package com.yurwar.simplepasswordstorage;

import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
public class SimplePasswordStorageApplication {

    public static String plainText = "This is a plain text which need to be encrypted by Java AES 256 GCM Encryption Algorithm";
    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    @SneakyThrows
    public static void main(String[] args) {
//        SpringApplication.run(SimplePasswordStorageApplication.class, args);

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);

        SecretKey key = keyGenerator.generateKey();
        byte[] IV = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        System.out.println("Original Text : " + plainText);

        byte[] cipherText = encrypt(plainText.getBytes(), key, IV);
        System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(cipherText));

        String decryptedText = decrypt(cipherText, key, IV);
        System.out.println("Decrypted Text : " + decryptedText);
    }

    @SneakyThrows
    private static byte[] encrypt(byte[] data, SecretKey key, byte[] IV) {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        return cipher.doFinal(data);
    }

    @SneakyThrows
    private static String decrypt(byte[] data, SecretKey key, byte[] IV) {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] decryptedText = cipher.doFinal(data);

        return new String(decryptedText);
    }

}
