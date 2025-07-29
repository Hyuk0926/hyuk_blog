document.addEventListener('DOMContentLoaded', function() {
    const sliders = document.querySelectorAll('.project-demo-slider');
    
    sliders.forEach(slider => {
        const track = slider.querySelector('.slider-track');
        const dots = slider.querySelectorAll('.slider-dot');
        let currentIndex = 0;
        
        // 도트 클릭 이벤트
        dots.forEach(dot => {
            dot.addEventListener('click', () => {
                const index = parseInt(dot.dataset.index);
                currentIndex = index;
                updateSlider();
            });
        });

        function updateSlider() {
            // 슬라이드 이동
            const offset = currentIndex * -100;
            track.style.transform = `translateX(${offset}%)`;
            
            // 도트 상태 업데이트
            dots.forEach((dot, index) => {
                dot.classList.toggle('active', index === currentIndex);
            });
        }

        // 자동 슬라이드 (5초마다)
        setInterval(() => {
            currentIndex = (currentIndex + 1) % dots.length;
            updateSlider();
        }, 5000);
    });
}); 