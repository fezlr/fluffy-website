document.addEventListener('DOMContentLoaded', () => {

    const tabs = document.querySelectorAll('.tab');
    const contents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            contents.forEach(c => c.classList.remove('active'));

            tab.classList.add('active');

            const target = document.getElementById('tab-' + tab.dataset.tab);
            if (target) target.classList.add('active');
        });
    });

    const followBtn = document.getElementById('follow-btn');

    if (followBtn) {
        followBtn.addEventListener('click', async () => {

            const userId = followBtn.dataset.id;
            const isFollowing = followBtn.classList.contains('following');

            try {
                const response = await fetch(`/api/v1/friends/${userId}/follow`, {
                    method: isFollowing ? 'DELETE' : 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
                    }
                });

                if (!response.ok) throw new Error();

                followBtn.textContent = isFollowing ? 'Follow' : 'Following';
                followBtn.classList.toggle('following');

            } catch {
                alert('Error');
            }
        });
    }

});