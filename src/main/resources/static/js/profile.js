document.querySelectorAll('.tab').forEach(tab => {
    tab.addEventListener('click', () => {
        document.querySelectorAll('.tab').forEach(t => t.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));

        tab.classList.add('active');
        document.getElementById('tab-' + tab.dataset.tab).classList.add('active');
    });
});

const followBtn = document.getElementById('follow-btn');
if (followBtn) {
    followBtn.addEventListener('click', async () => {
        const userId = followBtn.dataset.id;
        const isFollowing = followBtn.textContent.trim() === 'Following';

        const response = await fetch(`/api/v1/friends/${userId}/follow`, {
            method: isFollowing ? 'DELETE' : 'POST',
            headers: {
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
            }
        });

        if (response.ok) {
            followBtn.textContent = isFollowing ? 'Follow' : 'Following';
            followBtn.classList.toggle('following', !isFollowing);
        }
    });
}