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
        // 암호화 예제 (실제 비밀번호는 환경변수나 별도 파일에서 관리)
        System.out.println("=== 비밀번호 암호화 예제 ===");
        System.out.println("실제 사용 시에는 환경변수나 별도 설정 파일에서 비밀번호를 가져와야 합니다.");
        
        // 예시 비밀번호 (실제로는 사용하지 않음)
        String examplePassword = "your_password_here";
        String encryptedPassword = encrypt(examplePassword);
        System.out.println("Example Password: " + examplePassword);
        System.out.println("Encrypted Password: " + encryptedPassword);
        System.out.println("application-prod.yml에서 사용할 값: ENC(" + encryptedPassword + ")");
        
        // 복호화 테스트
        String decryptedPassword = decrypt(encryptedPassword);
        System.out.println("\n=== 복호화 테스트 ===");
        System.out.println("Decrypted Password: " + decryptedPassword);
        
        System.out.println("\n=== 사용법 ===");
        System.out.println("1. 실제 비밀번호를 환경변수로 설정");
        System.out.println("2. 이 클래스의 main 메서드를 실행하여 암호화된 값 생성");
        System.out.println("3. application-prod.yml에 ENC(암호화된값) 형태로 저장");
    }
} 