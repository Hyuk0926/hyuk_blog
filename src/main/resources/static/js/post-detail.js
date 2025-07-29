// Toast 알림 함수
function showCopyToast(msg) {
    // 이미 떠있는 토스트가 있으면 제거
    const old = document.getElementById('copy-toast');
    if (old) old.remove();
    const toast = document.createElement('div');
    toast.className = 'copy-toast';
    toast.id = 'copy-toast';
    toast.innerText = msg;
    document.body.appendChild(toast);
    setTimeout(() => {
        toast.remove();
    }, 1500);
}

// 코드 복사 버튼 동적 추가 및 복사 기능
document.addEventListener('DOMContentLoaded', function() {
    const content = document.getElementById('post-content');
    if (!content) return;
    
    // 모든 <pre><code>...</code></pre>를 찾아서 래핑
    content.querySelectorAll('pre > code').forEach(function(codeElem) {
        const pre = codeElem.parentElement;
        // 이미 래핑되어 있으면 중복 방지
        if (pre.parentElement.classList.contains('code-block-wrapper')) return;
        
        // 래퍼 div 생성
        const wrapper = document.createElement('div');
        wrapper.className = 'code-block-wrapper';
        pre.parentNode.insertBefore(wrapper, pre);
        wrapper.appendChild(pre);
        
        // 복사 버튼 생성
        const btn = document.createElement('button');
        btn.className = 'copy-btn';
        btn.type = 'button';
        btn.innerText = '📋';
        btn.onclick = function() {
            // 코드 텍스트 복사
            const code = codeElem.innerText;
            navigator.clipboard.writeText(code).then(function() {
                showCopyToast('복사 완료');
            });
        };
        wrapper.appendChild(btn);
    });
}); 