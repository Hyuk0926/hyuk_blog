package com.example.hyuk_blog.config;

import com.example.hyuk_blog.util.SimpleEncryptor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EncryptedPropertySource {
    
    @Component
    @ConfigurationPropertiesBinding
    public static class EncryptedPropertyResolver {
        
        public String resolveEncryptedValue(String value) {
            if (value != null && value.startsWith("ENC(") && value.endsWith(")")) {
                String encryptedValue = value.substring(4, value.length() - 1);
                return SimpleEncryptor.decrypt(encryptedValue);
            }
            return value;
        }
    }
    
    public static PropertySource<?> createPropertySource() {
        Map<String, Object> properties = new HashMap<>();
        
        // 암호화된 데이터베이스 비밀번호
        String encryptedPassword = "CzuAGRkXOyiECqlkyIio0w==";
        properties.put("spring.datasource.password", "ENC(" + encryptedPassword + ")");
        
        return new MapPropertySource("encryptedProperties", properties);
    }
} 