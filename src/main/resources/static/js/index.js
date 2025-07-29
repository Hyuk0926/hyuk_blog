document.addEventListener('DOMContentLoaded', () => {
    const container = document.querySelector('.page-container');
    const postCountMsg = container.dataset.postCountMsg;
    const noPostsMsg = container.dataset.noPostsMsg;
    const noImageMsg = container.dataset.noImageMsg;
    
    // 현재 언어 감지 (URL 파라미터 또는 기본값)
    const urlParams = new URLSearchParams(window.location.search);
    const currentLang = urlParams.get('lang') || 'ko';
    
    // 언어별 카운트 메시지 가져오기
    const getCountMessage = () => {
        if (currentLang === 'ja') {
            return container.dataset.postCountJa || '個';
        }
        return container.dataset.postCountKo || '개';
    };

    const searchInput = document.getElementById('search-input');
    const postGrid = document.getElementById('post-grid');
    const postCount = document.querySelector('.post-count');
    const categoryItems = document.querySelectorAll('.category-item');
    let debounceTimeout;

    function fetchAndRenderPosts(url) {
        fetch(url)
            .then(response => response.json())
            .then(posts => {
                console.log('Fetched posts:', posts); // 디버깅 로그
                updatePostGrid(posts);
            })
            .catch(error => {
                console.error('Error fetching posts:', error);
            });
    }

    function updatePostGrid(posts) {
        postGrid.innerHTML = '';
        
        // 현재 언어에 맞는 카운트 메시지 사용
        const countUnit = getCountMessage();
        const countText = posts.length + ' ' + countUnit;
        postCount.textContent = countText;
        console.log('Updated count:', countText); // 디버깅 로그

        if (posts.length === 0) {
            postGrid.innerHTML = `<div class="no-posts"><h3>${noPostsMsg}</h3></div>`;
            return;
        }

        posts.forEach(post => {
            const postElement = document.createElement('a');
            postElement.href = `/post/${post.id}?lang=${currentLang}`;
            postElement.className = 'post-card';

            const imageHtml = post.imageUrl
                ? `<img src="${post.imageUrl}" alt="${currentLang === 'ja' ? post.titleJa : post.titleKo}" class="post-image" />`
                : `<div class="post-image-placeholder"><span>${noImageMsg}</span></div>`;

            // 현재 언어에 맞는 제목과 요약 사용
            const title = currentLang === 'ja' ? post.titleJa : post.titleKo;
            const summary = currentLang === 'ja' ? post.summaryJa : post.summaryKo;
            const summaryHtml = summary ? `<p class="post-summary">${summary}</p>` : '';
            
            const formattedDate = new Date(post.createdAt).toLocaleDateString('ko-KR', {
                year: 'numeric', month: '2-digit', day: '2-digit'
            }).replace(/\. /g, '.').slice(0, -1);

            postElement.innerHTML = `
                <div class="post-image-wrapper">${imageHtml}</div>
                <div class="post-content">
                    <h3 class="post-title">${title}</h3>
                    ${summaryHtml}
                    <div class="post-meta">
                        <span class="post-date">${formattedDate}</span>
                    </div>
                </div>
            `;
            postGrid.appendChild(postElement);
        });
    }

    // 검색 기능
    searchInput.addEventListener('input', (e) => {
        const query = e.target.value.trim();
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(() => {
            if (query.length > 0) {
                fetchAndRenderPosts(`/api/search?q=${encodeURIComponent(query)}&lang=${currentLang}`);
            } else {
                const activeCategory = document.querySelector('.category-item.active').dataset.category;
                const url = activeCategory ? `/api/posts?category=${activeCategory}&lang=${currentLang}` : `/api/posts?lang=${currentLang}`;
                fetchAndRenderPosts(url);
            }
        }, 300);
    });

    // 카테고리 필터링
    categoryItems.forEach(item => {
        item.addEventListener('click', () => {
            const category = item.dataset.category;
            categoryItems.forEach(i => i.classList.remove('active'));
            item.classList.add('active');
            searchInput.value = ''; // Clear search input
            
            // 카테고리별 게시물 가져오기 (언어 정보 포함)
            const url = category ? `/api/posts?category=${category}&lang=${currentLang}` : `/api/posts?lang=${currentLang}`;
            fetchAndRenderPosts(url);
        });
    });
}); 