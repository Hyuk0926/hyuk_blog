document.addEventListener('DOMContentLoaded', () => {
    const container = document.querySelector('.page-container');
    const postCountMsg = container.dataset.postCountMsg;
    const noPostsMsg = container.dataset.noPostsMsg;
    const noImageMsg = container.dataset.noImageMsg;

    const searchInput = document.getElementById('search-input');
    const postGrid = document.getElementById('post-grid');
    const postCount = document.querySelector('.post-count');
    const categoryItems = document.querySelectorAll('.category-item');
    let debounceTimeout;

    function fetchAndRenderPosts(url) {
        fetch(url)
            .then(response => response.json())
            .then(posts => {
                updatePostGrid(posts);
            })
            .catch(error => {
                console.error('Error fetching posts:', error);
            });
    }

    function updatePostGrid(posts) {
        postGrid.innerHTML = '';
        
        // 카운트 메시지 업데이트 - 단위 포함
        const countText = posts.length + ' ' + postCountMsg;
        postCount.textContent = countText;

        if (posts.length === 0) {
            postGrid.innerHTML = `<div class="no-posts"><h3>${noPostsMsg}</h3></div>`;
            return;
        }

        posts.forEach(post => {
            const postElement = document.createElement('a');
            postElement.href = `/post/${post.id}`;
            postElement.className = 'post-card';

            const imageHtml = post.imageUrl
                ? `<img src="${post.imageUrl}" alt="${post.titleKo}" class="post-image" />`
                : `<div class="post-image-placeholder"><span>${noImageMsg}</span></div>`;

            const summaryHtml = post.summaryKo ? `<p class="post-summary">${post.summaryKo}</p>` : '';
            
            const formattedDate = new Date(post.createdAt).toLocaleDateString('ko-KR', {
                year: 'numeric', month: '2-digit', day: '2-digit'
            }).replace(/\. /g, '.').slice(0, -1);

            postElement.innerHTML = `
                <div class="post-image-wrapper">${imageHtml}</div>
                <div class="post-content">
                    <h3 class="post-title">${post.titleKo}</h3>
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
                fetchAndRenderPosts(`/api/search?q=${encodeURIComponent(query)}`);
            } else {
                const activeCategory = document.querySelector('.category-item.active').dataset.category;
                fetchAndRenderPosts(activeCategory ? `/api/posts?category=${activeCategory}` : '/api/posts');
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
            fetchAndRenderPosts(category ? `/api/posts?category=${category}` : '/api/posts');
        });
    });
}); 