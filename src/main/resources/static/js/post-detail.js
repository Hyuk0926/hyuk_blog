// Toast 알림 함수 - 개선된 버전
function showCopyToast(msg) {
    // 이미 떠있는 토스트가 있으면 제거
    const old = document.getElementById('copy-toast');
    if (old) old.remove();
    
    const toast = document.createElement('div');
    toast.className = 'copy-toast';
    toast.id = 'copy-toast';
    toast.innerHTML = `
        <span style="display: flex; align-items: center; gap: 8px;">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="flex-shrink: 0;">
                <polyline points="20,6 9,17 4,12"></polyline>
            </svg>
            <span>${msg}</span>
        </span>
    `;
    document.body.appendChild(toast);
    
    // 애니메이션 완료 후 요소 제거
    setTimeout(() => {
        if (toast.parentNode) {
            toast.remove();
        }
    }, 2000);
}

// 알림 메시지 함수들
function showNotification(message, duration = 5000) {
    const container = document.getElementById('notification-container');
    const text = document.getElementById('notification-text');
    
    if (container && text) {
        text.textContent = message;
        container.style.display = 'block';
        
        // 자동으로 사라지게 설정
        if (duration > 0) {
            setTimeout(() => {
                closeNotification();
            }, duration);
        }
    }
}

function closeNotification() {
    const container = document.getElementById('notification-container');
    if (container) {
        container.style.animation = 'slideOutRight 0.3s ease-out';
        setTimeout(() => {
            container.style.display = 'none';
            container.style.animation = '';
        }, 300);
    }
}

// 다국어 메시지 가져오기 함수
function getMessage(key) {
    // 현재 언어 감지
    const currentLang = document.documentElement.lang || 'ko';
    
    // 메시지 매핑
    const messages = {
        ko: {
            'comment.empty': '아직 댓글이 없습니다.',
            'comment.load.error': '댓글을 불러오는 중 오류가 발생했습니다.',
            'comment.content.required': '댓글 내용을 입력해주세요.',
            'comment.session.expired': '세션이 만료되었습니다. 다시 로그인해주세요.',
            'comment.server.error': '서버 오류가 발생했습니다.',
            'comment.write.error': '댓글 작성 중 오류가 발생했습니다.'
        },
        ja: {
            'comment.empty': 'まだコメントがありません。',
            'comment.load.error': 'コメントの読み込み中にエラーが発生しました。',
            'comment.content.required': 'コメント内容を入力してください。',
            'comment.session.expired': 'セッションが期限切れです。再度ログインしてください。',
            'comment.server.error': 'サーバーエラーが発生しました。',
            'comment.write.error': 'コメント投稿中にエラーが発生しました。'
        }
    };
    
    return messages[currentLang]?.[key] || messages.ko[key] || key;
}

// 코드 복사 버튼 동적 추가 및 복사 기능 - 개선된 버전
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
        btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg>';
        btn.title = '코드 복사';
        
        // 복사 기능 개선
        btn.onclick = function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            // 버튼 비활성화 (중복 클릭 방지)
            btn.disabled = true;
            btn.style.opacity = '0.5';
            
            // 코드 텍스트 추출 (Prism.js 하이라이팅 제거)
            let codeText = '';
            if (codeElem.textContent) {
                codeText = codeElem.textContent;
            } else {
                // fallback: innerText 사용
                codeText = codeElem.innerText;
            }
            
            // 클립보드에 복사
            if (navigator.clipboard && window.isSecureContext) {
                // 모던 브라우저에서 안전한 컨텍스트
                navigator.clipboard.writeText(codeText).then(function() {
                    showCopyToast('코드가 복사되었습니다');
                    btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20,6 9,17 4,12"></polyline></svg>';
                    setTimeout(() => {
                        btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg>';
                        btn.disabled = false;
                        btn.style.opacity = '';
                    }, 1000);
                }).catch(function(err) {
                    console.error('클립보드 복사 실패:', err);
                    fallbackCopyTextToClipboard(codeText, btn);
                });
            } else {
                // fallback: 구형 브라우저 지원
                fallbackCopyTextToClipboard(codeText, btn);
            }
        };
        
        wrapper.appendChild(btn);
    });
    
    // Prism.js로 코드 하이라이팅 적용
    if (typeof Prism !== 'undefined') {
        Prism.highlightAll();
    }
});

// 구형 브라우저를 위한 fallback 복사 함수
function fallbackCopyTextToClipboard(text, btn) {
    const textArea = document.createElement('textarea');
    textArea.value = text;
    textArea.style.position = 'fixed';
    textArea.style.left = '-999999px';
    textArea.style.top = '-999999px';
    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();
    
    try {
        const successful = document.execCommand('copy');
        if (successful) {
            showCopyToast('코드가 복사되었습니다');
            btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20,6 9,17 4,12"></polyline></svg>';
            setTimeout(() => {
                btn.innerHTML = '<svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"></rect><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"></path></svg>';
                btn.disabled = false;
                btn.style.opacity = '';
            }, 1000);
        } else {
            showCopyToast('복사에 실패했습니다');
            btn.disabled = false;
            btn.style.opacity = '';
        }
    } catch (err) {
        console.error('Fallback 복사 실패:', err);
        showCopyToast('복사에 실패했습니다');
        btn.disabled = false;
        btn.style.opacity = '';
    }
    
    document.body.removeChild(textArea);
}

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

// 모달 관련 전역 변수
let loginModalCallback = null;

// 모달 표시 함수
function showLoginModal(title, message, callback) {
    const modal = document.getElementById('login-confirm-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalMessage = document.getElementById('modal-message');
    
    if (modal && modalTitle && modalMessage) {
        modalTitle.textContent = title;
        modalMessage.textContent = message;
        loginModalCallback = callback;
        modal.style.display = 'flex';
        
        // 모달 외부 클릭 시 닫기 (이벤트 리스너 중복 방지)
        const handleModalClick = function(e) {
            if (e.target === modal) {
                closeLoginModal();
            }
        };
        
        const handleEscKey = function(e) {
            if (e.key === 'Escape') {
                closeLoginModal();
            }
        };
        
        // 기존 이벤트 리스너 제거 후 새로 추가
        modal.removeEventListener('click', handleModalClick);
        document.removeEventListener('keydown', handleEscKey);
        
        modal.addEventListener('click', handleModalClick);
        document.addEventListener('keydown', handleEscKey);
    }
}

// 모달 닫기 함수
function closeLoginModal() {
    const modal = document.getElementById('login-confirm-modal');
    if (modal) {
        modal.style.display = 'none';
        loginModalCallback = null;
    }
}

// 로그인 확인 함수
function confirmLogin() {
    if (loginModalCallback) {
        loginModalCallback();
    }
    closeLoginModal();
}

// 좋아요 기능 JavaScript
document.addEventListener('DOMContentLoaded', function() {
    const likeButton = document.querySelector('.like-button');
    if (!likeButton) return;
    
    // 로그인 상태 확인
    const isLoggedIn = document.querySelector('.comment-form') !== null; // 댓글 폼이 있으면 로그인된 상태
    
    // 로그인하지 않은 경우 좋아요 버튼 비활성화
    if (!isLoggedIn) {
        likeButton.style.opacity = '0.6';
        likeButton.style.cursor = 'not-allowed';
        likeButton.title = '좋아요 기능을 사용하려면 로그인이 필요합니다.';
    }
    
    likeButton.addEventListener('click', function() {
        // 로그인하지 않은 경우 클릭 무시
        if (!isLoggedIn) {
            const lang = this.getAttribute('data-lang') || 'ko';
            const title = lang === 'ja' ? 'ログインが必要' : '로그인 필요';
            const message = lang === 'ja' ? 
                'いいね機能を使用するにはログインが必要です。ログインページに移動しますか？' : 
                '좋아요 기능을 사용하려면 로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?';
            
            showLoginModal(title, message, function() {
                window.location.href = `/user/login?redirectUrl=${encodeURIComponent(window.location.pathname + window.location.search)}`;
            });
            return;
        }
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
            credentials: 'same-origin'
        })
        .then(response => {
            console.log('Like response status:', response.status);
            if (response.status === 401) {
                // 로그인이 필요한 경우
                const lang = likeButton.getAttribute('data-lang') || 'ko';
                const title = lang === 'ja' ? 'ログインが必要' : '로그인 필요';
                const message = lang === 'ja' ? 
                    'いいね機能を使用するにはログインが必要です。ログインページに移動しますか？' : 
                    '좋아요 기능을 사용하려면 로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?';
                showLoginModal(title, message, function() {
                    window.location.href = `/user/login?redirectUrl=${encodeURIComponent(window.location.pathname + window.location.search)}`;
                });
                return null;
            } else if (response.status === 403) {
                // 권한이 없는 경우 (세션 만료 등)
                console.error('Forbidden error in like request');
                const lang = likeButton.getAttribute('data-lang') || 'ko';
                const title = lang === 'ja' ? 'セッション期限切れ' : '세션 만료';
                const message = lang === 'ja' ? 
                    'セッションが期限切れです。再度ログインしてください。' : 
                    '세션이 만료되었습니다. 다시 로그인해주세요.';
                showLoginModal(title, message, function() {
                    window.location.href = `/user/login?redirectUrl=${encodeURIComponent(window.location.pathname + window.location.search)}`;
                });
                return null;
            } else if (response.status === 500) {
                console.error('Server error in like request');
                const lang = likeButton.getAttribute('data-lang') || 'ko';
                const serverErrorMessage = lang === 'ja' ? 'サーバーエラーが発生しました。' : '서버 오류가 발생했습니다.';
                showNotification(serverErrorMessage);
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
            const lang = likeButton.getAttribute('data-lang') || 'ko';
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
    console.log('댓글 기능 초기화 시작');
    
    // postId와 lang을 더 안전하게 가져오기
    const likeButton = document.querySelector('.like-button');
    let postId = likeButton ? likeButton.getAttribute('data-post-id') : null;
    const lang = likeButton ? likeButton.getAttribute('data-lang') : 'ko'; // 기본값 설정
    
    console.log('초기 postId:', postId, 'lang:', lang);
    
    // postId가 없으면 URL에서 추출
    if (!postId) {
        const pathParts = window.location.pathname.split('/');
        const urlPostId = pathParts[pathParts.length - 1];
        console.log('URL에서 추출한 postId:', urlPostId);
        if (urlPostId && !isNaN(urlPostId)) {
            postId = urlPostId;
        }
    }
    
    console.log('최종 postId:', postId);
    
    // postId가 여전히 없으면 댓글 기능 비활성화
    if (!postId) {
        console.error('게시글 ID를 찾을 수 없습니다.');
        return;
    }
    
    const commentList = document.getElementById('comment-list');
    const commentForm = document.querySelector('.comment-form');
    const contentTextarea = document.getElementById('comment-content');
    const submitBtn = document.getElementById('comment-submit');
    
    console.log('댓글 관련 요소들:', {
        commentList: commentList,
        commentForm: commentForm,
        contentTextarea: contentTextarea,
        submitBtn: submitBtn
    });
    
    // 댓글 목록 로드
    function loadComments() {
        if (!commentList) {
            console.error('댓글 목록 요소를 찾을 수 없습니다.');
            return;
        }
        
        const apiEndpoint = lang === 'ja' ? `/api/comments/jp/${postId}` : `/api/comments/kr/${postId}`;
        
        console.log('댓글 로드 시작:', apiEndpoint);
        console.log('현재 lang:', lang, 'postId:', postId);
        console.log('commentList 요소:', commentList);
        
        // 로그인하지 않은 사용자도 댓글을 볼 수 있도록 credentials 옵션 제거
        fetch(apiEndpoint + '?v=' + Date.now(), {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Cache-Control': 'no-cache'
            }
        })
            .then(response => {
                console.log('댓글 API 응답 상태:', response.status);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('댓글 로드 완료:', data);
                commentList.innerHTML = '';
                
                const comments = data.comments || [];
                const currentUserId = data.currentUserId;
                
                if (comments && comments.length > 0) {
                    comments.forEach(comment => {
                        commentList.appendChild(createCommentElement(comment, currentUserId));
                    });
                } else {
                    commentList.innerHTML = `<div style="text-align: center; color: #666; padding: 20px;">${getMessage('comment.empty')}</div>`;
                }
            })
            .catch(error => {
                console.error('댓글 로드 중 오류:', error);
                if (commentList) {
                    commentList.innerHTML = `<div style="text-align: center; color: #666; padding: 20px;">${getMessage('comment.load.error')}</div>`;
                }
            });
    }
    
    // 댓글 요소 생성
    function createCommentElement(comment, currentUserId) {
        const commentDiv = document.createElement('div');
        commentDiv.className = 'comment-item';
        commentDiv.dataset.commentId = comment.id;
        
        const date = new Date(comment.createdAt);
        const formattedDate = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
        
        // 로그인 상태 확인 (댓글 폼이 있으면 로그인된 상태)
        const isLoggedIn = document.querySelector('.comment-form') !== null;
        // admin 계정인지 확인 (ID가 1 또는 2인 경우)
        const isAdmin = currentUserId && (currentUserId === 1 || currentUserId === 2);
        // 현재 사용자가 댓글 작성자인지 확인
        const isCommentAuthor = currentUserId && comment.userId && currentUserId === comment.userId;
        // admin은 모든 댓글 삭제 가능, 일반 사용자는 자신의 댓글만 수정/삭제 가능
        const canEdit = isLoggedIn && isCommentAuthor;
        const canDelete = isLoggedIn && (isCommentAuthor || isAdmin);
        console.log('댓글 생성 시 로그인 상태:', isLoggedIn, 'admin 여부:', isAdmin, '댓글 작성자 여부:', isCommentAuthor, 'currentUserId:', currentUserId, 'comment.userId:', comment.userId);
        
        commentDiv.innerHTML = `
            <div class="comment-header">
                <div class="comment-info">
                    <div class="comment-nickname">${comment.nickname}</div>
                    <div class="comment-date">
                        ${formattedDate}
                        ${(comment.isEdited || comment.edited) ? '<span class="edited-badge"> (수정됨)</span>' : ''}
                    </div>
                </div>
                ${canDelete ? `
                <div class="comment-actions">
                    ${canEdit ? `<button class="comment-action-btn edit-btn">수정</button>` : ''}
                    <button class="comment-action-btn delete delete-btn">${isAdmin && !isCommentAuthor ? '관리자 삭제' : '삭제'}</button>
                </div>
                ` : ''}
            </div>
            <div class="comment-content">${comment.content}</div>
            ${canEdit ? `
            <div class="comment-edit-form">
                <textarea class="comment-edit-textarea">${comment.content}</textarea>
                <div class="comment-edit-buttons">
                    <button class="comment-edit-btn save">저장</button>
                    <button class="comment-edit-btn cancel">취소</button>
                </div>
            </div>
            ` : ''}
        `;
        
        // 수정 버튼 이벤트 (로그인한 사용자만)
        const editBtn = commentDiv.querySelector('.edit-btn');
        const editForm = commentDiv.querySelector('.comment-edit-form');
        const editTextarea = commentDiv.querySelector('.comment-edit-textarea');
        const saveBtn = commentDiv.querySelector('.save');
        const cancelBtn = commentDiv.querySelector('.cancel');
        
        if (editBtn) {
            editBtn.addEventListener('click', function() {
                editForm.classList.add('active');
                editTextarea.focus();
            });
        }
                
        if (saveBtn) {
            saveBtn.addEventListener('click', function() {
                const newContent = editTextarea.value.trim();
                if (!newContent) {
                    showNotification(getMessage('comment.content.required'));
                    return;
                }
                
                // 댓글 수정은 기존 엔드포인트 사용 (comment.id로 특정 댓글 수정)
                fetch(`/api/comments/${comment.id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `content=${encodeURIComponent(newContent)}`,
                    credentials: 'same-origin'
                })
                .then(response => {
                    if (response.status === 401) {
                        showNotification(getMessage('comment.login.required'));
                        return null;
                    } else if (response.status === 403) {
                        showNotification(getMessage('comment.session.expired'));
                        return null;
                    }
                    return response.json();
                })
                .then(data => {
                                         if (data && data.success) {
                         editForm.classList.remove('active');
                         commentDiv.querySelector('.comment-content').textContent = newContent;
                         setTimeout(() => {
                             loadComments(); // 댓글 목록 새로고침
                         }, 100);
                     } else if (data) {
                        showNotification(data.message);
                    }
                });
            });
        }
        
        if (cancelBtn) {
            cancelBtn.addEventListener('click', function() {
                editForm.classList.remove('active');
                editTextarea.value = comment.content;
            });
        }
        
        // 삭제 버튼 이벤트 (로그인한 사용자만)
        const deleteBtn = commentDiv.querySelector('.delete-btn');
        if (deleteBtn) {
            deleteBtn.addEventListener('click', function() {
                const lang = likeButton ? likeButton.getAttribute('data-lang') : 'ko';
                const title = lang === 'ja' ? 'コメント削除' : '댓글 삭제';
                const message = lang === 'ja' ? '本当にこのコメントを削除しますか？' : '정말로 이 댓글을 삭제하시겠습니까?';
                
                showLoginModal(title, message, function() {
                    // 댓글 삭제는 기존 엔드포인트 사용 (comment.id로 특정 댓글 삭제)
                    fetch(`/api/comments/${comment.id}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        credentials: 'same-origin'
                    })
                    .then(response => {
                        if (response.status === 401) {
                            showNotification(getMessage('comment.login.required'));
                            return null;
                        } else if (response.status === 403) {
                            showNotification(getMessage('comment.session.expired'));
                            return null;
                        }
                        return response.json();
                    })
                    .then(data => {
                        if (data && data.success) {
                            commentDiv.remove();
                        } else if (data) {
                            showNotification(data.message);
                        }
                    });
                });
            });
        }
        
        return commentDiv;
    }
    
    // 댓글 작성 (로그인한 사용자만)
    if (submitBtn && contentTextarea) {
        submitBtn.addEventListener('click', function() {
        const content = contentTextarea.value.trim();
        
        if (!content) {
            showNotification(getMessage('comment.content.required'));
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
            credentials: 'same-origin'
        })
        .then(response => {
            console.log('Comment response status:', response.status);
            if (response.status === 401) {
                // 로그인이 필요한 경우
                const lang = likeButton ? likeButton.getAttribute('data-lang') : 'ko';
                const title = lang === 'ja' ? 'ログインが必要' : '로그인 필요';
                const message = lang === 'ja' ? 
                    'コメントを投稿するにはログインが必要です。ログインページに移動しますか？' : 
                    '댓글을 작성하려면 로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?';
                showLoginModal(title, message, function() {
                    window.location.href = `/user/login?redirectUrl=${encodeURIComponent(window.location.pathname + window.location.search)}`;
                });
                return null;
            } else if (response.status === 403) {
                // 권한이 없는 경우 (세션 만료 등)
                console.error('Forbidden error in comment request');
                const lang = likeButton ? likeButton.getAttribute('data-lang') : 'ko';
                const title = lang === 'ja' ? 'セッション期限切れ' : '세션 만료';
                const message = lang === 'ja' ? 
                    'セッションが期限切れです。再度ログインしてください。' : 
                    '세션이 만료되었습니다. 다시 로그인해주세요.';
                showLoginModal(title, message, function() {
                    window.location.href = `/user/login?redirectUrl=${encodeURIComponent(window.location.pathname + window.location.search)}`;
                });
                return null;
            } else if (response.status === 500) {
                console.error('Server error in comment request');
                showNotification(getMessage('comment.server.error'));
                return null;
            }
            return response.json();
        })
        .then(comment => {
            if (comment) {
                // 폼 초기화
                contentTextarea.value = '';
                
                                 // 댓글 목록 새로고침
                 setTimeout(() => {
                     loadComments();
                 }, 100);
            }
        })
        .catch(error => {
            console.error('댓글 작성 중 오류:', error);
            showNotification(getMessage('comment.write.error'));
        });
        });
    }
    
    // 엔터키로 댓글 작성 (로그인한 사용자만)
    if (contentTextarea && submitBtn) {
        contentTextarea.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                submitBtn.click();
            }
        });
    }
    
    // 초기 댓글 로드 (로그인 여부와 관계없이 항상 실행)
    console.log('초기 댓글 로드 시작');
    console.log('로그인 상태:', commentForm ? '로그인됨' : '로그인 안됨');
    
    if (commentList) {
        // 약간의 지연을 두어 DOM이 완전히 로드된 후 댓글 로드
        setTimeout(() => {
            loadComments();
        }, 100);
    } else {
        console.error('댓글 목록 요소가 없어서 댓글을 로드할 수 없습니다.');
    }
});