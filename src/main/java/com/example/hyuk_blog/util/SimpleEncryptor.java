package com.example.hyuk_blog.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class SimpleEncryptor {
    
    private static final String DEFAULT_SECRET_KEY = "HyukBlogSecretKey"; // 16자리 키
    private static final String ALGORITHM = "AES";
    
    private static String getSecretKey() {
        // 환경 변수에서 키를 가져오거나 기본값 사용
        String envKey = System.getenv("ENCRYPTION_KEY");
        return envKey != null ? envKey : DEFAULT_SECRET_KEY;
    }
    
    public static String encrypt(String value) {
        try {
            String secretKey = getSecretKey();
            // 키를 16바이트로 맞춤
            byte[] keyBytes = secretKey.getBytes("UTF-8");
            byte[] key16Bytes = new byte[16];
            System.arraycopy(keyBytes, 0, key16Bytes, 0, Math.min(keyBytes.length, 16));
            
            SecretKeySpec secretKeySpec = new SecretKeySpec(key16Bytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    public static String decrypt(String encryptedValue) {
        try {
            String secretKey = getSecretKey();
            // 키를 16바이트로 맞춤
            byte[] keyBytes = secretKey.getBytes("UTF-8");
            byte[] key16Bytes = new byte[16];
            System.arraycopy(keyBytes, 0, key16Bytes, 0, Math.min(keyBytes.length, 16));
            
            SecretKeySpec secretKeySpec = new SecretKeySpec(key16Bytes, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    public static void main(String[] args) {
        // 비밀번호 암호화 예제
        String originalPassword = "Shka991204!";
        String encryptedPassword = encrypt(originalPassword);
        System.out.println("Original Password: " + originalPassword);
        System.out.println("Encrypted Password: " + encryptedPassword);
        
        // 복호화 테스트
        String decryptedPassword = decrypt(encryptedPassword);
        System.out.println("Decrypted Password: " + decryptedPassword);
    }
} 