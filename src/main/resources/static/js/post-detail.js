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
        
        // 언어 감지 및 클래스 추가
        detectAndAddLanguageClass(codeElem);
        
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
    
    // Prism.js로 코드 하이라이팅 적용
    if (typeof Prism !== 'undefined') {
        Prism.highlightAll();
    }
});

// 언어 감지 및 클래스 추가 함수
function detectAndAddLanguageClass(codeElement) {
    const code = codeElement.textContent || codeElement.innerText;
    const firstLine = code.split('\n')[0].trim();
    
    // 언어 감지 로직
    let language = 'text';
    
    if (firstLine.includes('java') || firstLine.includes('public class') || firstLine.includes('import java')) {
        language = 'java';
    } else if (firstLine.includes('javascript') || firstLine.includes('function') || firstLine.includes('const ') || firstLine.includes('let ')) {
        language = 'javascript';
    } else if (firstLine.includes('python') || firstLine.includes('def ') || firstLine.includes('import ')) {
        language = 'python';
    } else if (firstLine.includes('html') || firstLine.includes('<html') || firstLine.includes('<div')) {
        language = 'html';
    } else if (firstLine.includes('css') || firstLine.includes('{') && firstLine.includes(':')) {
        language = 'css';
    } else if (firstLine.includes('sql') || firstLine.includes('SELECT') || firstLine.includes('CREATE TABLE')) {
        language = 'sql';
    } else if (firstLine.includes('xml') || firstLine.includes('<xml')) {
        language = 'xml';
    } else if (firstLine.includes('json') || firstLine.includes('{') && firstLine.includes('"')) {
        language = 'json';
    } else if (firstLine.includes('bash') || firstLine.includes('#!/bin/bash') || firstLine.includes('curl ')) {
        language = 'bash';
    } else if (firstLine.includes('gradle') || firstLine.includes('build.gradle')) {
        language = 'gradle';
    }
    
    // 언어 클래스 추가
    codeElement.className = `language-${language}`;
    codeElement.parentElement.className = `language-${language}`;
} 