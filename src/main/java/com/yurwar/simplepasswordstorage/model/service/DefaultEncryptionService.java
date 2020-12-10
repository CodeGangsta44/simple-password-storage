package com.yurwar.simplepasswordstorage.model.service;

import com.yurwar.simplepasswordstorage.controller.dto.KeyDto;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.support.VaultResponseSupport;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Service
public class DefaultEncryptionService implements EncryptionService {
    private static final String KEK_STORAGE_PATH = "password-storage-kek";
    private final VaultKeyValueOperations keyValueOperations;

    public DefaultEncryptionService(VaultKeyValueOperations keyValueOperations) {
        this.keyValueOperations = keyValueOperations;
    }

    //JUST FOR TESTING PURPOSES
    //REAL KEY SHOULD BE GENERATED EXTERNALLY AND STORED TO THE PROD VAULT
    @PostConstruct
    public void init() {
        KeyGenerator keyGen;
        try {
            int KEK_SIZE = 256;
            String KEK_ALGORITHM = "AES";

            keyGen = KeyGenerator.getInstance(KEK_ALGORITHM);
            keyGen.init(KEK_SIZE);

            SecretKey secretKey = keyGen.generateKey();
            byte[] encodedKey = secretKey.getEncoded();
            String baseKek = new String(Base64.getEncoder().encode(encodedKey));
            KeyDto key = new KeyDto(baseKek);
            keyValueOperations.put(KEK_STORAGE_PATH, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getKeyEncryptionKey() {
        String encodedKey = (String) Optional
                .ofNullable(keyValueOperations.get(KEK_STORAGE_PATH))
                .map(VaultResponseSupport::getData)
                .map(dataMap -> dataMap.get("key"))
                .orElseThrow();

        return Base64.getDecoder().decode(encodedKey);
    }
}
