package com.yurwar.simplepasswordstorage.converter;

import javax.persistence.AttributeConverter;
import java.nio.charset.StandardCharsets;

public class EncryptedAttributeConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final byte[] KEY = "KeeyKeeyKeeyKeeyKeeyKeeyKeeyKeey".getBytes();

    @Override
    public String convertToDatabaseColumn(String s) {
        return null;
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return null;
    }
}
