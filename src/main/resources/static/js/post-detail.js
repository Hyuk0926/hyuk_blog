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
                    const mainPage = lang === 'ja' ? '/jp' : '/index';
                    window.location.href = `${mainPage}?lang=${lang}`;
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
        
        // 언어에 따라 적절한 API 엔드포인트 사용
        const apiEndpoint = lang === 'ja' ? `/api/like/jp/${postId}` : `/api/like/kr/${postId}`;
        
        console.log('Sending like request:', {
            postId: postId,
            lang: lang,
            url: apiEndpoint
        });
        
        fetch(apiEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest',
                'Cache-Control': 'no-cache'
            },
            credentials: 'include'
        })
        .then(response => {
            console.log('Like response status:', response.status);
            if (response.status === 401) {
                // 로그인이 필요한 경우
                const lang = document.querySelector('.like-button').getAttribute('data-lang');
                const loginRequiredMessage = lang === 'ja' ? 
                    'いいね機能を使用するにはログインが必要です。ログインページに移動しますか？' : 
                    '좋아요 기능을 사용하려면 로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?';
                if (confirm(loginRequiredMessage)) {
                    window.location.href = `/user/login?redirectUrl=${encodeURIComponent(window.location.pathname + window.location.search)}`;
                }
                return null;
            } else if (response.status === 500) {
                console.error('Server error in like request');
                const lang = document.querySelector('.like-button').getAttribute('data-lang');
                const serverErrorMessage = lang === 'ja' ? 'サーバーエラーが発生しました。' : '서버 오류가 발생했습니다.';
                alert(serverErrorMessage);
                return null;
            }
            return response.json();
        })
        .then(data => {
            if (data) {
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
            }
        })
        .catch(error => {
            const lang = document.querySelector('.like-button').getAttribute('data-lang');
            const errorMessage = lang === 'ja' ? 'いいね処理中にエラーが発生しました' : '좋아요 처리 중 오류 발생';
            console.error(errorMessage + ':', error);
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
    const contentTextarea = document.getElementById('comment-content');
    const submitBtn = document.getElementById('comment-submit');
    
    // 현재 페이지의 언어를 확인하여 적절한 API 엔드포인트 사용
    const lang = document.querySelector('.like-button').getAttribute('data-lang');
    
    // 댓글 목록 로드
    function loadComments() {
        const apiEndpoint = lang === 'ja' ? `/api/comments/jp/${postId}` : `/api/comments/kr/${postId}`;
        
        fetch(apiEndpoint + '?v=' + Date.now())
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
                            editForm.classList.add('active');
                            editTextarea.focus();
                        });
                
                saveBtn.addEventListener('click', function() {
                    const newContent = editTextarea.value.trim();
                    if (!newContent) {
                        alert('댓글 내용을 입력해주세요.');
                        return;
                    }
                    
                    // 댓글 수정은 기존 엔드포인트 사용 (comment.id로 특정 댓글 수정)
                    fetch(`/api/comments/${comment.id}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: `content=${encodeURIComponent(newContent)}`
                    })
                    .then(response => {
                        if (response.status === 401) {
                            alert('로그인이 필요합니다.');
                            return null;
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data && data.success) {
                            editForm.classList.remove('active');
                            commentDiv.querySelector('.comment-content').textContent = newContent;
                            loadComments(); // 댓글 목록 새로고침
                        } else if (data) {
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
                // 댓글 삭제는 기존 엔드포인트 사용 (comment.id로 특정 댓글 삭제)
                fetch(`/api/comments/${comment.id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    }
                })
                .then(response => {
                    if (response.status === 401) {
                        alert('로그인이 필요합니다.');
                        return null;
                    }
                    return response.json();
                })
                .then(data => {
                    if (data && data.success) {
                        commentDiv.remove();
                    } else if (data) {
                        alert(data.message);
                    }
                });
            }
        });
        
        return commentDiv;
    }
    
    // 댓글 작성
    submitBtn.addEventListener('click', function() {
        const content = contentTextarea.value.trim();
        
        if (!content) {
            alert('댓글 내용을 입력해주세요.');
            return;
        }
        
        const formData = new URLSearchParams();
        formData.append('content', content);
        
        const apiEndpoint = lang === 'ja' ? `/api/comments/jp/${postId}` : `/api/comments/kr/${postId}`;
        
        console.log('Sending comment request:', {
            postId: postId,
            lang: lang,
            apiEndpoint: apiEndpoint,
            content: content,
            formData: formData.toString()
        });
        
        fetch(apiEndpoint + '?v=' + Date.now(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-Requested-With': 'XMLHttpRequest',
                'Cache-Control': 'no-cache'
            },
            body: formData,
            credentials: 'include'
        })
        .then(response => {
            console.log('Comment response status:', response.status);
            if (response.status === 401) {
                // 로그인이 필요한 경우
                if (confirm('댓글을 작성하려면 로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?')) {
                    window.location.href = `/user/login?redirectUrl=${encodeURIComponent(window.location.pathname + window.location.search)}`;
                }
                return null;
            } else if (response.status === 500) {
                console.error('Server error in comment request');
                alert('서버 오류가 발생했습니다.');
                return null;
            }
            return response.json();
        })
        .then(comment => {
            if (comment) {
                // 폼 초기화
                contentTextarea.value = '';
                
                // 댓글 목록 새로고침
                loadComments();
            }
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