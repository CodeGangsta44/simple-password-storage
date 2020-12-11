package com.yurwar.simplepasswordstorage.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;

@Configuration
public class VaultConfiguration extends AbstractVaultConfiguration {
    @Value("${VAULT_TOKEN}")
    private String token;

    @Bean
    @NonNull
    public VaultKeyValueOperations vaultKeyValueOperations() {
        return vaultTemplate().opsForKeyValue("secret", KeyValueBackend.versioned());
    }

    @Override
    @NonNull
    public VaultEndpoint vaultEndpoint() {

        VaultEndpoint endpoint = new VaultEndpoint();
        endpoint.setScheme("http");
        return endpoint;
    }

    @Override
    @NonNull
    public ClientAuthentication clientAuthentication() {
        return new TokenAuthentication(token);
    }

}
