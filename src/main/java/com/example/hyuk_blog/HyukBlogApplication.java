package com.example.hyuk_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HyukBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(HyukBlogApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?> protocol) {
                protocol.setMaxSwallowSize(-1); // 무제한
                connector.setProperty("maxFileCount", "20"); // 파일 개수 제한 늘림
            }
        });
    }
}
