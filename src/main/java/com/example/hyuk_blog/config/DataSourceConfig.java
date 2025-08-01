package com.example.hyuk_blog.config;

import com.example.hyuk_blog.util.SimpleEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    
    @Value("${spring.datasource.url}")
    private String url;
    
    @Value("${spring.datasource.username}")
    private String username;
    
    @Value("${spring.datasource.password}")
    private String password;
    
    @Bean
    @Primary
    public DataSource dataSource() {
        // 암호화된 비밀번호 복호화
        String decryptedPassword = password;
        if (password.startsWith("ENC(") && password.endsWith(")")) {
            String encryptedValue = password.substring(4, password.length() - 1);
            decryptedPassword = SimpleEncryptor.decrypt(encryptedValue);
        }
        
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(decryptedPassword)
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
} 