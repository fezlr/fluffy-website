const token = document.querySelector('meta[name="_csrf"]').content;
const header = document.querySelector('meta[name="_csrf_header"]').content;

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

    const friendBtn = document.getElementById('friend-btn');

    if (friendBtn) {
        friendBtn.addEventListener('click', async () => {
            const userId = friendBtn.dataset.id;
            const isPending = friendBtn.classList.contains('pending');
            const isFriend = friendBtn.classList.contains('friend');

            const url = isFriend
                ? `/api/v1/friend/delete/${userId}?senderId=${currentUserId}`
                : `/api/v1/friend-request/${isPending ? 'delete' : 'create'}/${userId}?senderId=${currentUserId}`;
            const method = (isPending || isFriend) ? 'DELETE' : 'POST';

            try {
                const response = await fetch(url, {
                    method,
                    headers: { [header]: token, 'Content-Type': 'application/json' }
                });

                if (!response.ok) throw new Error();

                if (isFriend || isPending) {
                    friendBtn.textContent = 'Add a friend';
                    friendBtn.classList.remove('friend', 'pending');
                } else {
                    friendBtn.textContent = 'Pending';
                    friendBtn.classList.add('pending');
                }
            } catch {
                alert('Error');
            }
        });
    }
});