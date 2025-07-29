document.addEventListener('DOMContentLoaded', function() {
    const passwordInput = document.getElementById('password');
    const capsLockMsg = document.getElementById('capsLockMsg');
    const togglePassword = document.getElementById('togglePassword');

    // 비밀번호 입력 시 영어로 자동 전환 (IME off)
    passwordInput.addEventListener('focus', function() {
        this.setAttribute('inputmode', 'latin');
        this.style.imeMode = 'disabled';
    });

    // CapsLock 알림
    function checkCapsLock(e) {
        if (e.getModifierState && e.getModifierState('CapsLock')) {
            capsLockMsg.style.display = 'block';
        } else {
            capsLockMsg.style.display = 'none';
        }
    }

    passwordInput.addEventListener('keyup', checkCapsLock);
    passwordInput.addEventListener('keydown', checkCapsLock);

    // 비밀번호 보기 토글
    togglePassword.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);
        this.style.color = type === 'text' ? '#007bff' : '#888';
    });
}); 