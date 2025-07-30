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

// 언어 변경 시 자연스러운 처리
document.addEventListener('DOMContentLoaded', function() {
    const langLinks = document.querySelectorAll('a[href*="lang="]');
    const currentPostId = window.location.pathname.split('/').pop().split('?')[0];
    
    langLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const url = new URL(this.href);
            const lang = url.searchParams.get('lang');
            
            // 현재 게시글의 해당 언어 버전이 있는지 확인
            fetch(`/api/post/${currentPostId}?lang=${lang}`)
                .then(response => {
                    if (response.ok) {
                        // 해당 언어 버전이 있으면 그곳으로 이동
                        return response.json();
                    } else {
                        // 없으면 해당 언어의 메인 페이지로 이동
                        throw new Error('Post not found in this language');
                    }
                })
                .then(post => {
                    // 해당 언어 버전으로 이동
                    window.location.href = `/post/${currentPostId}?lang=${lang}`;
                })
                .catch(error => {
                    // 해당 언어 버전이 없으면 메인 페이지로 이동
                    window.location.href = `/index?lang=${lang}`;
                });
        });
    });
});

// 좋아요 기능 JavaScript
document.addEventListener('DOMContentLoaded', function() {
    const likeButton = document.querySelector('.like-button');
    if (!likeButton) return;
    
    likeButton.addEventListener('click', function() {
        const postId = this.getAttribute('data-post-id');
        const lang = this.getAttribute('data-lang');
        const heartIcon = this.querySelector('.heart-icon');
        const likeCount = this.querySelector('.like-count');
        
        // 버튼 비활성화 (중복 클릭 방지)
        this.disabled = true;
        
        fetch(`/api/like/${postId}?lang=${lang}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => response.json())
        .then(data => {
            // 좋아요 상태 업데이트
            if (data.liked) {
                heartIcon.classList.add('liked');
                heartIcon.textContent = '♥';
            } else {
                heartIcon.classList.remove('liked');
                heartIcon.textContent = '♡';
            }
            
            // 좋아요 수 업데이트
            likeCount.textContent = data.likeCount;
        })
        .catch(error => {
            console.error('좋아요 처리 중 오류 발생:', error);
        })
        .finally(() => {
            // 버튼 다시 활성화
            this.disabled = false;
        });
    });
});

// 댓글 기능 JavaScript
document.addEventListener('DOMContentLoaded', function() {
    const postId = document.querySelector('.like-button').getAttribute('data-post-id');
    const commentList = document.getElementById('comment-list');
    const commentForm = document.querySelector('.comment-form');
    const nicknameInput = document.getElementById('comment-nickname');
    const passwordInput = document.getElementById('comment-password');
    const contentTextarea = document.getElementById('comment-content');
    const submitBtn = document.getElementById('comment-submit');
    
    // 모달 관련 요소들
    const modal = document.getElementById('password-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalPassword = document.getElementById('modal-password');
    const modalConfirm = document.getElementById('modal-confirm');
    const modalCancel = document.getElementById('modal-cancel');
    
    let currentAction = null;
    let currentCommentId = null;
    let currentEditTextarea = null;
    
    // 모달 표시 함수
    function showModal(title, callback) {
        modalTitle.textContent = title;
        modalPassword.value = '';
        currentAction = callback;
        modal.classList.add('active');
        modalPassword.focus();
    }
    
    // 모달 숨기기 함수
    function hideModal() {
        modal.classList.remove('active');
        currentAction = null;
        currentCommentId = null;
        currentEditTextarea = null;
    }
    
    // 모달 이벤트 리스너
    modalConfirm.addEventListener('click', function() {
        const password = modalPassword.value.trim();
        if (!password) {
            alert('비밀번호를 입력해주세요.');
            return;
        }
        
        if (currentAction) {
            currentAction(password);
        }
        hideModal();
    });
    
    modalCancel.addEventListener('click', hideModal);
    
    // 모달 외부 클릭 시 닫기
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            hideModal();
        }
    });
    
    // 엔터키로 모달 확인
    modalPassword.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            modalConfirm.click();
        }
    });
    
    // 댓글 목록 로드
    function loadComments() {
        fetch(`/api/comments/${postId}`)
            .then(response => response.json())
            .then(comments => {
                commentList.innerHTML = '';
                comments.forEach(comment => {
                    commentList.appendChild(createCommentElement(comment));
                });
            })
            .catch(error => {
                console.error('댓글 로드 중 오류:', error);
            });
    }
    
    // 댓글 요소 생성
    function createCommentElement(comment) {
        const commentDiv = document.createElement('div');
        commentDiv.className = 'comment-item';
        commentDiv.dataset.commentId = comment.id;
        
        const date = new Date(comment.createdAt);
        const formattedDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
        
        commentDiv.innerHTML = `
            <div class="comment-header">
                <div class="comment-info">
                    <div class="comment-nickname">${comment.nickname}</div>
                    <div class="comment-date">
                        ${formattedDate}
                        ${comment.isEdited ? '<span class="edited-badge"> (수정됨)</span>' : ''}
                    </div>
                </div>
                <div class="comment-actions">
                    <button class="comment-action-btn edit-btn">수정</button>
                    <button class="comment-action-btn delete delete-btn">삭제</button>
                </div>
            </div>
            <div class="comment-content">${comment.content}</div>
            <div class="comment-edit-form">
                <textarea class="comment-edit-textarea">${comment.content}</textarea>
                <div class="comment-edit-buttons">
                    <button class="comment-edit-btn save">저장</button>
                    <button class="comment-edit-btn cancel">취소</button>
                </div>
            </div>
        `;
        
        // 수정 버튼 이벤트
        const editBtn = commentDiv.querySelector('.edit-btn');
        const editForm = commentDiv.querySelector('.comment-edit-form');
        const editTextarea = commentDiv.querySelector('.comment-edit-textarea');
        const saveBtn = commentDiv.querySelector('.save');
        const cancelBtn = commentDiv.querySelector('.cancel');
        
                        editBtn.addEventListener('click', function() {
                    showModal('댓글 수정을 위한 비밀번호를 입력하세요', function(password) {
                        fetch(`/api/comments/${comment.id}/verify`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: `password=${encodeURIComponent(password)}`
                        })
                        .then(response => response.json())
                        .then(data => {
                            if (data.valid) {
                                editForm.classList.add('active');
                                editTextarea.focus();
                                // 비밀번호를 저장하여 저장 시 재사용
                                commentDiv.dataset.verifiedPassword = password;
                            } else {
                                alert('비밀번호가 일치하지 않습니다.');
                            }
                        });
                    });
                });
                
                saveBtn.addEventListener('click', function() {
                    const newContent = editTextarea.value.trim();
                    if (!newContent) {
                        alert('댓글 내용을 입력해주세요.');
                        return;
                    }
                    
                    const password = commentDiv.dataset.verifiedPassword;
                    if (!password) {
                        alert('비밀번호 인증이 필요합니다.');
                        return;
                    }
                    
                    fetch(`/api/comments/${comment.id}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `password=${encodeURIComponent(password)}&content=${encodeURIComponent(newContent)}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            editForm.classList.remove('active');
                            commentDiv.querySelector('.comment-content').textContent = newContent;
                            delete commentDiv.dataset.verifiedPassword; // 저장된 비밀번호 삭제
                            loadComments(); // 댓글 목록 새로고침
                        } else {
                            alert(data.message);
                        }
                    });
                });
        
        cancelBtn.addEventListener('click', function() {
            editForm.classList.remove('active');
            editTextarea.value = comment.content;
        });
        
        // 삭제 버튼 이벤트
        const deleteBtn = commentDiv.querySelector('.delete-btn');
        deleteBtn.addEventListener('click', function() {
            if (confirm('정말로 이 댓글을 삭제하시겠습니까?')) {
                showModal('댓글 삭제를 위한 비밀번호를 입력하세요', function(password) {
                    fetch(`/api/comments/${comment.id}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `password=${encodeURIComponent(password)}`
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            commentDiv.remove();
                        } else {
                            alert(data.message);
                        }
                    });
                });
            }
        });
        
        return commentDiv;
    }
    
    // 댓글 작성
    submitBtn.addEventListener('click', function() {
        const nickname = nicknameInput.value.trim();
        const password = passwordInput.value.trim();
        const content = contentTextarea.value.trim();
        
        if (!password) {
            alert('비밀번호를 입력해주세요.');
            return;
        }
        
        if (!content) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }
        
        const formData = new URLSearchParams();
        formData.append('nickname', nickname);
        formData.append('password', password);
        formData.append('content', content);
        
        fetch(`/api/comments/${postId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: formData
        })
        .then(response => response.json())
        .then(comment => {
            // 폼 초기화
            nicknameInput.value = '';
            passwordInput.value = '';
            contentTextarea.value = '';
            
            // 댓글 목록 새로고침
            loadComments();
        })
        .catch(error => {
            console.error('댓글 작성 중 오류:', error);
            alert('댓글 작성 중 오류가 발생했습니다.');
        });
    });
    
    // 엔터키로 댓글 작성
    contentTextarea.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            submitBtn.click();
        }
    });
    
    // 초기 댓글 로드
    loadComments();
});