let selectedChatId = null;

function selectChat(element) {
    document.querySelectorAll('.chat-card').forEach(c => c.classList.remove('active'));
    element.classList.add('active');

    selectedChatId = element.dataset.chatId;

    document.getElementById('selectedChatName').innerText = element.dataset.chatName;

    const avatar = element.dataset.chatPhoto || '/images/default-avatar.png';
    document.getElementById('selectedChatAvatar').src = avatar;

    document.getElementById('emptyChat').classList.add('hidden');
    document.getElementById('chatContainer').classList.remove('hidden');

    loadMessages();
}

function openEditChatModal() {
    const activeCard = document.querySelector('.chat-card.active');
    document.getElementById('editChatId').value = selectedChatId;
    document.getElementById('editChatName').value = activeCard.dataset.chatName;

    const preview = document.getElementById('editChatPhotoPreview');
    const currentPhoto = activeCard.dataset.chatPhoto;
    if (currentPhoto) {
        preview.src = currentPhoto;
        preview.style.display = 'block';
    } else {
        preview.style.display = 'none';
    }

    document.getElementById('editChatModal').classList.remove('hidden');
}

function closeEditChatModal() {
    document.getElementById('editChatModal').classList.add('hidden');
}

async function saveChat() {
    const csrf = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    const id = document.getElementById('editChatId').value;
    const name = document.getElementById('editChatName').value.trim();
    const fileInput = document.getElementById('editChatPhoto');

    if (!name) return;

    let photoUrl = document.querySelector('.chat-card.active')?.dataset.chatPhoto || null;

    if (fileInput.files[0]) {
        const formData = new FormData();
        formData.append('file', fileInput.files[0]);

        const uploadRes = await fetch('/api/v1/chats/upload-photo', {
            method: 'POST',
            headers: { [csrfHeader]: csrf },
            body: formData
        });

        if (!uploadRes.ok) return;
        const data = await uploadRes.json();
        photoUrl = data.url;
    }

    const res = await fetch(`/api/v1/chats/update/${id}`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json', [csrfHeader]: csrf },
        body: JSON.stringify({ name, photoUrl })
    });

    if (res.ok) location.reload();
}

async function loadMessages() {
    const container = document.getElementById('messagesContainer');
    container.innerHTML = '';

    try {
        const response = await fetch('/api/v1/messages/chat/' + selectedChatId);
        if (!response.ok) return;

        const messages = await response.json();

        messages.forEach(message => {
            const isOwn = message.userId === CURRENT_USER_ID;
            const avatar = message.userMainPhotoUrl || '/images/default-avatar.png';
            const time = new Date(message.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

            const el = document.createElement('div');
            el.className = isOwn ? 'message outgoing' : 'message incoming';
            el.innerHTML = `
                <img class="msg-avatar" src="${avatar}" alt="avatar">
                <div class="msg-body">
                    <span class="msg-name">${message.userName}</span>
                    <div class="msg-bubble">${message.text}</div>
                    <span class="msg-time">${time}</span>
                </div>
            `;
            container.appendChild(el);
        });

        container.scrollTop = container.scrollHeight;

    } catch (e) {
        console.error(e);
    }
}

async function sendMessage() {
    const input = document.getElementById('messageInput');
    const text = input.value.trim();

    if (!text || !selectedChatId) return;

    const csrf = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

    try {
        const response = await fetch('/api/v1/messages/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrf
            },
            body: JSON.stringify({
                chatId: selectedChatId,
                userId: CURRENT_USER_ID,
                text: text
            })
        });

        if (!response.ok) return;

        input.value = '';
        await loadMessages();

    } catch (e) {
        console.error(e);
    }
}

document.getElementById('messageInput')
    ?.addEventListener('keydown', function (e) {
        if (e.key === 'Enter') sendMessage();
    });

document.getElementById('chatSearch')
    ?.addEventListener('input', function () {
        const value = this.value.toLowerCase();
        document.querySelectorAll('.chat-card').forEach(chat => {
            const name = chat.dataset.chatName.toLowerCase();
            chat.style.display = name.includes(value) ? 'flex' : 'none';
        });
    });

document.addEventListener('DOMContentLoaded', () => {
    if (OPEN_CHAT_ID) {
        const card = document.querySelector(`[data-chat-id="${OPEN_CHAT_ID}"]`);
        if (card) selectChat(card);
    }
});