package com.yurwar.simplepasswordstorage.model.repository;

import com.yurwar.simplepasswordstorage.model.entity.User;
import com.yurwar.simplepasswordstorage.model.service.EncryptionService;
import lombok.SneakyThrows;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Objects;

@Component
public class EncryptedUserRepository {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    public static final int GCM_IV_LENGTH = 12;
    public static final int GCM_TAG_LENGTH = 16;

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    public EncryptedUserRepository(UserRepository userRepository, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    public User findUserByUsername(String username) {

        User user = userRepository.findUserByUsername(username);

        if (Objects.nonNull(user)) {
            decryptDek(user);
            decryptSensitiveData(user);
        }

        return user;
    }

    public void save(User user) {

        encryptSensitiveData(user);
        encryptDek(user);
        userRepository.save(user);
    }

    private void encryptSensitiveData(User user) {

        user.setAddress(encrypt(user.getAddress(), user.getDek()));
    }

    private void decryptSensitiveData(User user) {

        user.setAddress(decrypt(user.getAddress(), user.getDek()));
    }

    private void encryptDek(User user) {

        user.setDek(encrypt(user.getDek(), encryptionService.getKeyEncryptionKey()));
    }

    private void decryptDek(User user) {

        user.setDek(decrypt(user.getDek(), encryptionService.getKeyEncryptionKey()));
    }

    @SneakyThrows
    private static String encrypt(String data, String key) {
        byte[] keyBytes = Hex.decode(key);
        byte[] IV = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] cipherText = cipher.doFinal(data.getBytes());

        byte[] result = new byte[GCM_IV_LENGTH + cipherText.length];

        System.arraycopy(IV, 0, result, 0, IV.length);
        System.arraycopy(cipherText, 0, result, IV.length, cipherText.length);

        return new String(Hex.encode(result));
    }

    @SneakyThrows
    private static String decrypt(String data, String key) {
        byte[] keyBytes = Hex.decode(key);
        byte[] encryptedData = Hex.decode(data);

        byte[] IV = new byte[GCM_IV_LENGTH];
        byte[] cipherText = new byte[encryptedData.length - GCM_IV_LENGTH];

        System.arraycopy(encryptedData, 0, IV, 0, IV.length);
        System.arraycopy(encryptedData, IV.length, cipherText, 0, cipherText.length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, IV);

        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }
}
