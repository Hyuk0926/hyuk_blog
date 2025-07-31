// 사용자 관련 공통 유틸리티 함수들

// API 요청 공통 함수
const UserAPI = {
    // 중복 확인 요청
    checkDuplicate: async function(endpoint, data) {
        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams(data)
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('API 요청 오류:', error);
            throw error;
        }
    },
    
    // 폼 데이터 검증
    validateForm: function(formData) {
        const errors = [];
        
        // 필수 필드 검증
        const requiredFields = formData.required || [];
        requiredFields.forEach(field => {
            if (!formData[field] || !formData[field].trim()) {
                errors.push(`${field}는 필수 입력 항목입니다.`);
            }
        });
        
        // 이메일 형식 검증
        if (formData.email && formData.email.trim()) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(formData.email)) {
                errors.push('올바른 이메일 형식을 입력해주세요.');
            }
        }
        
        // 비밀번호 길이 검증
        if (formData.password && formData.password.length < 6) {
            errors.push('비밀번호는 최소 6자 이상이어야 합니다.');
        }
        
        return errors;
    }
};

// UI 관련 유틸리티
const UserUI = {
    // 입력 필드 상태 업데이트
    updateFieldStatus: function(field, isValid, message) {
        const validationElement = document.getElementById(`${field.id}-validation`);
        
        if (validationElement) {
            field.classList.remove('error', 'success');
            field.classList.add(isValid ? 'success' : 'error');
            
            validationElement.textContent = message;
            validationElement.className = `validation-message ${isValid ? 'success' : 'error'}`;
        }
    },
    
    // 로딩 상태 표시
    showLoading: function(button, text = '처리 중...') {
        if (button) {
            button.disabled = true;
            button.dataset.originalText = button.textContent;
            button.textContent = text;
        }
    },
    
    // 로딩 상태 해제
    hideLoading: function(button) {
        if (button && button.dataset.originalText) {
            button.disabled = false;
            button.textContent = button.dataset.originalText;
            delete button.dataset.originalText;
        }
    },
    
    // 메시지 표시
    showMessage: function(message, type = 'info') {
        const messageDiv = document.createElement('div');
        messageDiv.className = `message ${type}`;
        messageDiv.textContent = message;
        
        // 기존 메시지 제거
        const existingMessage = document.querySelector('.message');
        if (existingMessage) {
            existingMessage.remove();
        }
        
        // 새 메시지 추가
        const container = document.querySelector('.login-container, .register-container');
        if (container) {
            container.insertBefore(messageDiv, container.firstChild);
            
            // 3초 후 자동 제거
            setTimeout(() => {
                messageDiv.remove();
            }, 3000);
        }
    }
};

// 이벤트 리스너 등록 헬퍼
const UserEvents = {
    // 중복 확인 버튼 이벤트 등록
    setupDuplicateCheckButton: function(field, button, endpoint, fieldName) {
        button.addEventListener('click', async function() {
            const value = field.value.trim();
            const minLength = fieldName === 'username' ? 4 : 2;
            
            if (value.length < minLength) {
                UserUI.showMessage(`${fieldName === 'username' ? '아이디' : fieldName === 'nickname' ? '닉네임' : '이메일'}를 ${minLength}자 이상 입력해주세요.`, 'error');
                return;
            }
            
            // 버튼 비활성화 및 로딩 상태
            UserUI.showLoading(this, '확인 중...');
            
            try {
                const available = await UserAPI.checkDuplicate(endpoint, { [fieldName]: value });
                const message = available ? 
                    `사용 가능한 ${fieldName === 'username' ? '아이디' : fieldName === 'nickname' ? '닉네임' : '이메일'}입니다.` :
                    `이미 사용 중인 ${fieldName === 'username' ? '아이디' : fieldName === 'nickname' ? '닉네임' : '이메일'}입니다.`;
                
                UserUI.updateFieldStatus(field, available, message);
                
                // 성공 시 버튼 비활성화
                if (available) {
                    this.disabled = true;
                    this.textContent = '확인완료';
                    this.style.background = 'linear-gradient(145deg, #68d391 0%, #47a87a 100%)';
                }
            } catch (error) {
                UserUI.updateFieldStatus(field, false, '확인 중 오류가 발생했습니다.');
            } finally {
                UserUI.hideLoading(this);
            }
        });
        
        // 입력 필드 변경 시 버튼 재활성화
        field.addEventListener('input', function() {
            button.disabled = false;
            button.textContent = fieldName === 'username' ? '중복확인' : fieldName === 'nickname' ? '중복확인' : '중복확인';
            button.style.background = 'linear-gradient(145deg, #666666 0%, #444444 100%)';
        });
    },
    
    // 중복 확인 이벤트 등록 (기존 blur 이벤트)
    setupDuplicateCheck: function(field, endpoint, fieldName) {
        field.addEventListener('blur', async function() {
            const value = this.value.trim();
            const minLength = fieldName === 'username' ? 4 : 2;
            
            if (value.length >= minLength) {
                try {
                    const available = await UserAPI.checkDuplicate(endpoint, { [fieldName]: value });
                    const message = available ? 
                        `사용 가능한 ${fieldName === 'username' ? '아이디' : fieldName === 'nickname' ? '닉네임' : '이메일'}입니다.` :
                        `이미 사용 중인 ${fieldName === 'username' ? '아이디' : fieldName === 'nickname' ? '닉네임' : '이메일'}입니다.`;
                    
                    UserUI.updateFieldStatus(this, available, message);
                } catch (error) {
                    UserUI.updateFieldStatus(this, false, '확인 중 오류가 발생했습니다.');
                }
            }
        });
    },
    
    // 비밀번호 확인 이벤트 등록
    setupPasswordConfirm: function(passwordField, confirmField) {
        confirmField.addEventListener('input', function() {
            const isValid = this.value === passwordField.value;
            const message = isValid ? '비밀번호가 일치합니다.' : '비밀번호가 일치하지 않습니다.';
            UserUI.updateFieldStatus(this, isValid, message);
        });
    },
    
    // 캡스락 확인 이벤트 등록
    setupCapsLockCheck: function(field, warningElement) {
        field.addEventListener('keydown', function(e) {
            // Caps Lock 키 감지 (keyCode 20 또는 getModifierState 사용)
            if (e.getModifierState && e.getModifierState('CapsLock')) {
                warningElement.classList.add('show');
            } else if (e.keyCode === 20) {
                // Caps Lock 키를 눌렀을 때
                setTimeout(() => {
                    if (e.getModifierState && e.getModifierState('CapsLock')) {
                        warningElement.classList.add('show');
                    } else {
                        warningElement.classList.remove('show');
                    }
                }, 100);
            }
        });
        
        field.addEventListener('keyup', function(e) {
            if (e.getModifierState && e.getModifierState('CapsLock')) {
                warningElement.classList.add('show');
            } else {
                warningElement.classList.remove('show');
            }
        });
        
        field.addEventListener('blur', function() {
            warningElement.classList.remove('show');
        });
    }
};

// 전역으로 사용할 수 있도록 window 객체에 추가
window.UserAPI = UserAPI;
window.UserUI = UserUI;
window.UserEvents = UserEvents; 