const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

document.addEventListener('DOMContentLoaded', () => {
    const userId = document.getElementById('current-user-id').value;

    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('tab-btn--active'));
            btn.classList.add('tab-btn--active');
            document.querySelectorAll('.tab-panel').forEach(p => p.style.display = 'none');
            document.getElementById(`tab-${btn.dataset.tab}`).style.display = 'block';
        });
    });

    document.getElementById('search-input').addEventListener('input', function () {
        const q = this.value.toLowerCase();
        document.querySelectorAll('.friend-card, .request-card').forEach(card => {
            const name = card.querySelector('.friend-name').textContent.toLowerCase();
            card.style.display = name.includes(q) ? '' : 'none';
        });
    });

    document.querySelectorAll('.btn-cancel-request').forEach(btn => {
        btn.addEventListener('click', async () => {
            const receiverId = btn.dataset.id;
            btn.disabled = true;
            try {
                const res = await fetch(`/api/v1/friend-request/delete/${receiverId}?senderId=${userId}`, {
                    method: 'DELETE',
                    headers: { [csrfHeader]: csrfToken }
                });
                if (!res.ok) throw new Error();
                btn.closest('.request-card').remove();
            } catch (e) {
                console.error(e);
                btn.disabled = false;
            }
        });
    });

    document.querySelectorAll('.btn-accept-request').forEach(btn => {
        btn.addEventListener('click', async () => {
            const senderId = btn.dataset.id;
            btn.disabled = true;
            try {
                const res = await fetch(`/api/v1/friend-request/create/${senderId}?senderId=${userId}`, {
                    method: 'POST',
                    headers: { [csrfHeader]: csrfToken }
                });
                if (!res.ok) throw new Error();
                btn.closest('.request-card').remove();
            } catch (e) {
                console.error(e);
                btn.disabled = false;
            }
        });
    });
});