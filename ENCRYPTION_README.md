# 데이터베이스 비밀번호 암호화 설정

## 개요
이 프로젝트는 데이터베이스 비밀번호를 AES 암호화를 사용하여 보호합니다.

## 암호화 방법

### 1. 비밀번호 암호화
```bash
# SimpleEncryptor 클래스 실행
java -cp build/classes/java/main com.example.hyuk_blog.util.SimpleEncryptor
```

### 2. 암호화된 비밀번호 사용
```yaml
spring:
  datasource:
    password: ENC(암호화된_비밀번호)
```

## 보안 설정

### 1. 환경 변수 사용 (권장)
운영 환경에서는 환경 변수를 통해 암호화 키를 설정하세요:

```bash
# Linux/Mac
export ENCRYPTION_KEY="your-secret-key-here"

# Windows
set ENCRYPTION_KEY=your-secret-key-here
```

### 2. AWS Elastic Beanstalk 환경 변수
AWS EB 콘솔에서 환경 변수를 설정:
- `ENCRYPTION_KEY`: 암호화 키
- `SPRING_PROFILES_ACTIVE`: prod

## 환경별 설정

### 개발 환경 (application-dev.yml)
- 로컬 MySQL 사용
- SQL 로그 활성화
- 디버그 로그 활성화

### 운영 환경 (application-prod.yml)
- AWS RDS 사용
- SQL 로그 비활성화
- 보안 로그 설정

## 주의사항

1. **암호화 키 보안**: 암호화 키는 절대 소스 코드에 하드코딩하지 마세요.
2. **환경 변수**: 운영 환경에서는 반드시 환경 변수를 사용하세요.
3. **키 관리**: 암호화 키는 안전한 방법으로 관리하고 정기적으로 변경하세요.
4. **백업**: 암호화 키를 잃어버리면 데이터에 접근할 수 없습니다.

## 문제 해결

### 암호화 오류
- 암호화 키가 올바른지 확인
- 환경 변수가 설정되었는지 확인

### 복호화 오류
- 암호화된 값이 올바른 형식인지 확인
- 암호화 키가 암호화 시와 동일한지 확인 