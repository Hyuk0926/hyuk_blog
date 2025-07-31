// 로그인 페이지 JavaScript
document.addEventListener('DOMContentLoaded', function() {
    // DOM 요소들
    const loginForm = document.querySelector('form[action="/user/login"]');
    const username = document.getElementById('username');
    const password = document.getElementById('password');
    const submitBtn = document.querySelector('.login-button');
    const togglePassword = document.getElementById('togglePassword');
    const capsLockMsg = document.getElementById('capsLockMsg');
    
    // 비밀번호 보이기/숨기기 기능
    togglePassword.addEventListener('click', function() {
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        this.textContent = type === 'password' ? '𝄐' : '𝄑';
        this.title = type === 'password' ? '비밀번호 보기' : '비밀번호 숨기기';
    });
    
    // 캡스락 확인 기능
    password.addEventListener('keydown', function(e) {
        if (e.getModifierState && e.getModifierState('CapsLock')) {
            capsLockMsg.style.display = 'block';
        } else if (e.keyCode === 20) {
            setTimeout(() => {
                if (e.getModifierState && e.getModifierState('CapsLock')) {
                    capsLockMsg.style.display = 'block';
                } else {
                    capsLockMsg.style.display = 'none';
                }
            }, 100);
        }
    });
    
    password.addEventListener('keyup', function(e) {
        if (e.getModifierState && e.getModifierState('CapsLock')) {
            capsLockMsg.style.display = 'block';
        } else {
            capsLockMsg.style.display = 'none';
        }
    });
    
    password.addEventListener('blur', function() {
        capsLockMsg.style.display = 'none';
    });
    
    // 폼 제출 전 검증
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            if (!username.value.trim() || !password.value.trim()) {
                e.preventDefault();
                UserUI.showMessage('아이디와 비밀번호를 모두 입력해주세요.', 'error');
                return;
            }
            
            // 로딩 상태 표시
            UserUI.showLoading(submitBtn, '로그인 중...');
        });
    }
    
    // 입력 필드 포커스 효과
    const formInputs = document.querySelectorAll('.form-input');
    formInputs.forEach(input => {
        input.addEventListener('focus', function() {
            this.parentElement.classList.add('focused');
        });
        
        input.addEventListener('blur', function() {
            if (!this.value.trim()) {
                this.parentElement.classList.remove('focused');
            }
        });
    });
    
    // 엔터키로 로그인
    document.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && (e.target === username || e.target === password)) {
            loginForm.submit();
        }
    });
}); 