package com.yurwar.simplepasswordstorage;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

@SpringBootApplication
public class SimplePasswordStorageApplication {

    static String DATA = "hghkgkghkghkghkghkghkgkhghkghkghkgghkghkvgvgvj";

    static String ALGORITHM = "AES/GCM/NoPadding";
    static String KEY = "KeeyKeeyKeeyKeeyKeeyKeeyKeeyKeey";

    @SneakyThrows
    public static void main(String[] args) {
//        SpringApplication.run(SimplePasswordStorageApplication.class, args);

        String result = encrypt(DATA, KEY);

        System.out.println(new String(Base64.getDecoder().decode(result.getBytes())).length() - DATA.length());

        System.out.println(decrypt(result, KEY));
    }

    @SneakyThrows
    private static String encrypt(String data, String key) {

        byte[] IV = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        GCMParameterSpec params = new GCMParameterSpec(128, IV);
        Key keySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, params);

        String result = new String(IV) + new String(cipher.doFinal(data.getBytes()));

        return new String(Base64.getEncoder().encode(result.getBytes()));
    }

    @SneakyThrows
    private static String decrypt(String data, String key) {

        String decodedData = new String(Base64.getDecoder().decode(data.getBytes()));
        byte[] IV = decodedData.substring(0, 12).getBytes();

        GCMParameterSpec params = new GCMParameterSpec(128, IV);
        Key keySpec = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, params);

        return new String(cipher.doFinal(decodedData.getBytes()));
    }

}
