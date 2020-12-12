package com.yurwar.simplepasswordstorage.configuration;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TlsConfiguration {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainerCustomizer() {
        return (factory) -> factory.addConnectorCustomizers((c) ->
                ((AbstractHttp11Protocol<?>) c.getProtocolHandler()).setUseServerCipherSuitesOrder(true));
    }
}
