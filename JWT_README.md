# JWT (JSON Web Token) 구현 가이드

## JWT란?

JWT(JSON Web Token)는 당사자 간에 정보를 JSON 객체로 안전하게 전송하기 위한 컴팩트하고 독립적인 방법을 정의하는 개방형 표준(RFC 7519)입니다.

### JWT 구조
JWT는 세 부분으로 구성됩니다:
1. **Header**: 토큰 타입과 서명 알고리즘 정보
2. **Payload**: 실제 데이터 (클레임)
3. **Signature**: 토큰의 무결성을 검증하는 서명

## 구현된 기능

### 1. JWT 유틸리티 (`JwtUtil`)
- 토큰 생성, 검증, 파싱
- 사용자명, 역할, 만료일 추출
- Bearer 접두사 처리

### 2. JWT 인증 필터 (`JwtAuthenticationFilter`)
- 모든 요청에서 JWT 토큰 검증
- 유효한 토큰을 Spring Security 컨텍스트에 설정

### 3. JWT 인증 서비스 (`JwtAuthService`)
- 관리자/사용자 로그인 처리
- 토큰 검증 및 정보 추출

### 4. JWT 인증 컨트롤러 (`JwtAuthController`)
- REST API 엔드포인트 제공
- 로그인, 토큰 검증, 정보 조회

## API 엔드포인트

### 1. 관리자 로그인
```
POST /api/auth/admin/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### 2. 사용자 로그인
```
POST /api/auth/user/login
Content-Type: application/json

{
  "username": "user",
  "password": "user123"
}
```

### 3. 토큰 검증
```
POST /api/auth/validate
Authorization: Bearer {token}
```

### 4. 토큰 정보 조회
```
GET /api/auth/info
Authorization: Bearer {token}
```

## 설정

### application.yml
```yaml
jwt:
  secret: hyukBlogJwtSecretKey2024ForDevelopmentOnlyChangeInProduction
  expiration: 86400000  # 24시간 (밀리초)
```

### SecurityConfig
- JWT 필터 추가
- 세션 비활성화 (STATELESS)
- API 경로별 권한 설정

## 사용 방법

### 1. 로그인
```javascript
// 관리자 로그인
const response = await fetch('/api/auth/admin/login', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
    },
    body: JSON.stringify({
        username: 'admin',
        password: 'admin123'
    })
});

const result = await response.json();
const token = result.token; // JWT 토큰
```

### 2. API 호출 시 토큰 사용
```javascript
// 보호된 API 호출
const response = await fetch('/admin/dashboard', {
    headers: {
        'Authorization': `Bearer ${token}`
    }
});
```

### 3. 토큰 검증
```javascript
// 토큰 유효성 검증
const response = await fetch('/api/auth/validate', {
    method: 'POST',
    headers: {
        'Authorization': `Bearer ${token}`
    }
});
```

## 테스트

JWT 테스트 페이지를 통해 모든 기능을 테스트할 수 있습니다:
```
http://localhost:5000/jwt-test.html
```

## 보안 고려사항

### 1. 시크릿 키
- 운영 환경에서는 강력한 시크릿 키 사용
- 환경 변수로 관리
- 정기적인 키 교체

### 2. 토큰 만료
- 적절한 만료 시간 설정 (24시간 권장)
- 리프레시 토큰 구현 고려

### 3. HTTPS 사용
- 운영 환경에서는 반드시 HTTPS 사용
- 토큰 전송 시 암호화

### 4. 토큰 저장
- 클라이언트에서 안전한 저장소 사용
- localStorage 대신 httpOnly 쿠키 고려

## 확장 가능한 기능

### 1. 리프레시 토큰
```java
// 리프레시 토큰 생성
public String generateRefreshToken(String username) {
    return Jwts.builder()
        .setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
        .signWith(getSigningKey(), Jwts.SIG.HS256)
        .compact();
}
```

### 2. 토큰 블랙리스트
```java
// 로그아웃 시 토큰 블랙리스트에 추가
public void logout(String token) {
    blacklistedTokens.add(token);
}
```

### 3. 역할 기반 접근 제어
```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() {
    // 관리자만 접근 가능
}
```

## 문제 해결

### 1. 토큰 만료
- 클라이언트에서 401 응답 시 자동 로그인 페이지로 리다이렉트
- 토큰 갱신 로직 구현

### 2. CORS 이슈
- 프론트엔드에서 Authorization 헤더 포함 시 CORS 설정 필요
- `@CrossOrigin(origins = "*")` 사용

### 3. 토큰 크기
- 클레임 정보 최소화
- 필요한 정보만 포함

## 참고 자료

- [JWT 공식 사이트](https://jwt.io/)
- [Spring Security JWT 가이드](https://spring.io/guides/tutorials/spring-security-and-angular-js/)
- [RFC 7519 - JSON Web Token](https://tools.ietf.org/html/rfc7519) 