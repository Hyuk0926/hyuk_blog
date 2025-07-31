// 회원가입 페이지 JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // DOM 요소들
    const username = document.getElementById('username');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const nickname = document.getElementById('nickname');
    const email = document.getElementById('email');
    const submitBtn = document.getElementById('submitBtn');
    const registerForm = document.getElementById('registerForm');
    
    // 중복확인 버튼들
    const usernameCheckBtn = document.getElementById('username-check-btn');
    const nicknameCheckBtn = document.getElementById('nickname-check-btn');
    const emailCheckBtn = document.getElementById('email-check-btn');
    
    // 캡스락 경고 요소들
    const passwordCapslock = document.getElementById('password-capslock');
    const confirmPasswordCapslock = document.getElementById('confirmPassword-capslock');
    
    // 중복확인 버튼 이벤트 설정
    UserEvents.setupDuplicateCheckButton(username, usernameCheckBtn, '/user/check-username', 'username');
    UserEvents.setupDuplicateCheckButton(nickname, nicknameCheckBtn, '/user/check-nickname', 'nickname');
    UserEvents.setupDuplicateCheckButton(email, emailCheckBtn, '/user/check-email', 'email');
    
    // 캡스락 확인 이벤트 설정
    UserEvents.setupCapsLockCheck(password, passwordCapslock);
    UserEvents.setupCapsLockCheck(confirmPassword, confirmPasswordCapslock);
    
    // 비밀번호 확인 이벤트 설정
    UserEvents.setupPasswordConfirm(password, confirmPassword);
    
    // 폼 제출 전 최종 검증
    registerForm.addEventListener('submit', function(e) {
        const hasErrors = document.querySelectorAll('.form-input.error').length > 0;
        const requiredFields = ['username', 'password', 'confirmPassword', 'nickname'];
        let missingFields = false;
        
        // 필수 필드 검증
        requiredFields.forEach(fieldName => {
            const field = document.getElementById(fieldName);
            if (!field.value.trim()) {
                missingFields = true;
                field.classList.add('error');
            }
        });
        
        // 중복확인 완료 여부 검증
        const uncheckedFields = [];
        if (!usernameCheckBtn.disabled) uncheckedFields.push('아이디');
        if (!nicknameCheckBtn.disabled) uncheckedFields.push('닉네임');
        
        if (uncheckedFields.length > 0) {
            e.preventDefault();
            UserUI.showMessage(`${uncheckedFields.join(', ')} 중복확인을 완료해주세요.`, 'error');
            return;
        }
        
        if (hasErrors || missingFields) {
            e.preventDefault();
            UserUI.showMessage('입력 정보를 확인해주세요.', 'error');
        } else {
            UserUI.showLoading(submitBtn, '회원가입 중...');
        }
    });
    
    // 입력 필드 실시간 검증
    username.addEventListener('input', function() {
        const value = this.value.trim();
        if (value.length > 0 && value.length < 4) {
            UserUI.updateFieldStatus(this, false, '아이디는 4자 이상이어야 합니다.');
        } else if (value.length > 20) {
            UserUI.updateFieldStatus(this, false, '아이디는 20자 이하여야 합니다.');
        } else if (value.length >= 4 && !/^[a-zA-Z0-9_]+$/.test(value)) {
            UserUI.updateFieldStatus(this, false, '영문, 숫자, 언더스코어만 사용 가능합니다.');
        } else if (value.length >= 4) {
            UserUI.updateFieldStatus(this, true, '');
        }
    });
    
    nickname.addEventListener('input', function() {
        const value = this.value.trim();
        if (value.length > 0 && value.length < 2) {
            UserUI.updateFieldStatus(this, false, '닉네임은 2자 이상이어야 합니다.');
        } else if (value.length > 20) {
            UserUI.updateFieldStatus(this, false, '닉네임은 20자 이하여야 합니다.');
        } else if (value.length >= 2) {
            UserUI.updateFieldStatus(this, true, '');
        }
    });
    
    password.addEventListener('input', function() {
        const value = this.value;
        if (value.length > 0 && value.length < 6) {
            UserUI.updateFieldStatus(this, false, '비밀번호는 6자 이상이어야 합니다.');
        } else if (value.length >= 6) {
            UserUI.updateFieldStatus(this, true, '');
        }
    });
}); 