// 커스텀 커서 JavaScript - 임시 비활성화
/*
document.addEventListener('DOMContentLoaded', function() {
    // 모바일에서는 커스텀 커서 비활성화
    if ('ontouchstart' in window) {
        document.body.style.cursor = 'auto';
        return;
    }
    
    const cursor = document.getElementById('custom-cursor');
    
    // 커서 요소가 없으면 생성
    if (!cursor) {
        const cursorElement = document.createElement('div');
        cursorElement.id = 'custom-cursor';
        cursorElement.className = 'custom-cursor';
        document.body.appendChild(cursorElement);
    }
    
    const cursorElement = document.getElementById('custom-cursor');
    
    // 마우스 움직임 추적
    document.addEventListener('mousemove', (e) => {
        cursorElement.style.left = e.clientX + 'px';
        cursorElement.style.top = e.clientY + 'px';
    });
    
    // 커서 상태 변경 함수
    function setCursorType(type) {
        // 모든 커서 클래스 제거
        cursorElement.className = 'custom-cursor';
        
        // 새로운 타입 추가
        if (type !== 'default') {
            cursorElement.classList.add(type);
        }
    }
    
    // 링크, 버튼 등 클릭 가능한 요소들
    const pointerElements = document.querySelectorAll('a, button, .nav-link, .category-item, .post-card, .admin-button, .auth-btn, .control-btn, .lang-btn, [role="button"]');
    pointerElements.forEach(element => {
        element.addEventListener('mouseenter', () => {
            setCursorType('pointer');
            cursorElement.classList.add('hover');
        });
        
        element.addEventListener('mouseleave', () => {
            setCursorType('default');
            cursorElement.classList.remove('hover');
        });
    });
    
    // 텍스트 입력 요소들
    const textElements = document.querySelectorAll('input[type="text"], input[type="email"], input[type="password"], textarea, [contenteditable="true"]');
    textElements.forEach(element => {
        element.addEventListener('mouseenter', () => {
            setCursorType('text');
        });
        
        element.addEventListener('mouseleave', () => {
            setCursorType('default');
        });
    });
    
    // 드래그 가능한 요소들
    const moveElements = document.querySelectorAll('[draggable="true"], .draggable');
    moveElements.forEach(element => {
        element.addEventListener('mouseenter', () => {
            setCursorType('move');
        });
        
        element.addEventListener('mouseleave', () => {
            setCursorType('default');
        });
    });
    
    // 리사이즈 가능한 요소들
    const resizeElements = document.querySelectorAll('.resizable, textarea');
    resizeElements.forEach(element => {
        element.addEventListener('mouseenter', (e) => {
            const rect = element.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            // 모서리 영역 감지
            if (x < 10 || x > rect.width - 10) {
                setCursorType('horizontal');
            } else if (y < 10 || y > rect.height - 10) {
                setCursorType('vertical');
            } else {
                setCursorType('text');
            }
        });
        
        element.addEventListener('mouseleave', () => {
            setCursorType('default');
        });
    });
    
    // 비활성화된 요소들
    const disabledElements = document.querySelectorAll('[disabled], .disabled, .unavailable');
    disabledElements.forEach(element => {
        element.addEventListener('mouseenter', () => {
            setCursorType('unavailable');
        });
        
        element.addEventListener('mouseleave', () => {
            setCursorType('default');
        });
    });
    
    // 로딩 상태 감지
    function checkLoadingState() {
        const loadingElements = document.querySelectorAll('.loading, [aria-busy="true"]');
        if (loadingElements.length > 0) {
            setCursorType('loading');
        } else {
            setCursorType('default');
        }
    }
    
    // 페이지 로딩 시 체크
    checkLoadingState();
    
    // 로딩 상태 변경 감지 (MutationObserver 사용)
    const observer = new MutationObserver(checkLoadingState);
    observer.observe(document.body, {
        attributes: true,
        attributeFilter: ['class', 'aria-busy'],
        subtree: true
    });
    
    // 대기 상태 (마우스가 움직이지 않을 때)
    let waitTimeout;
    document.addEventListener('mousemove', () => {
        clearTimeout(waitTimeout);
        setCursorType('default');
        
        waitTimeout = setTimeout(() => {
            setCursorType('wait');
        }, 3000); // 3초 후 대기 커서로 변경
    });
});
*/ 