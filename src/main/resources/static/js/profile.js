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
            const isFriending = friendBtn.classList.contains('friending');

            try {
                const action = isFriending ? 'delete' : 'create'
                const response = await fetch(`/api/v1/friend-request/${action}/${userId}?senderId=${currentUserId}`, {
                    method: isFriending ? 'DELETE' : 'POST',
                    headers: {
                        [header]: token,
                        'Content-Type': 'application/json',
                    }
                });

                if (!response.ok) throw new Error();

                friendBtn.textContent = isFriending ? 'Friend' : 'Friending';
                friendBtn.classList.toggle('friending');

            } catch {
                alert('Error');
            }
        });
    }

});