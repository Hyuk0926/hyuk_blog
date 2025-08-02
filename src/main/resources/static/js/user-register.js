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
    
    // 이메일 필드 초기 상태 설정
    if (!email.value.trim()) {
        emailCheckBtn.disabled = true;
        emailCheckBtn.textContent = window.i18nMessages ? window.i18nMessages.emailRequired : '이메일 입력 필요';
        emailCheckBtn.style.background = 'linear-gradient(145deg, #555555 0%, #333333 100%)';
    }
    
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
        
        // 아이디와 닉네임은 필수 중복확인
        if (usernameCheckBtn.dataset.checked !== 'true') uncheckedFields.push('아이디');
        if (nicknameCheckBtn.dataset.checked !== 'true') uncheckedFields.push('닉네임');
        
        // 이메일은 입력된 경우에만 중복확인 필수
        if (email.value.trim() && emailCheckBtn.dataset.checked !== 'true') {
            uncheckedFields.push('이메일');
        }
        
        if (uncheckedFields.length > 0) {
            e.preventDefault();
            UserUI.showMessage(`${uncheckedFields.join(', ')} 중복확인을 완료해주세요.`, 'error');
            console.log('중복확인 상태:', {
                username: usernameCheckBtn.dataset.checked,
                nickname: nicknameCheckBtn.dataset.checked,
                email: emailCheckBtn.dataset.checked,
                emailValue: email.value.trim()
            });
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
    
    // 이메일 필드 변경 시 중복확인 버튼 상태 관리
    email.addEventListener('input', function() {
        const value = this.value.trim();
        if (!value) {
            // 이메일이 비어있으면 중복확인 버튼 비활성화
            emailCheckBtn.disabled = true;
            emailCheckBtn.textContent = window.i18nMessages ? window.i18nMessages.emailRequired : '이메일 입력 필요';
            emailCheckBtn.style.background = 'linear-gradient(145deg, #555555 0%, #333333 100%)';
            // 체크 상태 초기화
            delete emailCheckBtn.dataset.checked;
        } else {
            // 이메일이 입력되면 중복확인 버튼 활성화
            emailCheckBtn.disabled = false;
            emailCheckBtn.textContent = window.i18nMessages ? window.i18nMessages.duplicateCheck : '중복확인';
            emailCheckBtn.style.background = 'linear-gradient(145deg, #666666 0%, #444444 100%)';
            // 체크 상태 초기화
            delete emailCheckBtn.dataset.checked;
        }
    });
}); 