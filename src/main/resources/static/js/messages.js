let selectedChatId = null;

function selectChat(element) {

    document.querySelectorAll('.chat-card')
        .forEach(card => card.classList.remove('active'));

    element.classList.add('active');

    selectedChatId = element.dataset.chatId;

    document.getElementById('selectedChatName').innerText =
        element.dataset.chatName;

    document.getElementById('emptyChat')
        .classList.add('hidden');

    document.getElementById('chatContainer')
        .classList.remove('hidden');

    loadMessages();
}

async function loadMessages() {

    const container =
        document.getElementById('messagesContainer');

    container.innerHTML = '';

    try {

        const response =
            await fetch('/api/v1/messages/chat/' + selectedChatId);

        if (!response.ok) {
            return;
        }

        const messages = await response.json();

        messages.forEach(message => {

            const isOwn =
                message.userId === CURRENT_USER_ID;

            const messageElement =
                document.createElement('div');

            messageElement.className =
                isOwn
                    ? 'message outgoing'
                    : 'message incoming';

            messageElement.innerHTML = `
                <div class="message-bubble">
                    ${message.text}
                </div>
            `;

            container.appendChild(messageElement);
        });

        container.scrollTop = container.scrollHeight;

    } catch (e) {
        console.error(e);
    }
}

async function sendMessage() {

    const input =
        document.getElementById('messageInput');

    const text = input.value.trim();

    if (!text || !selectedChatId) {
        return;
    }

    try {

        const response =
            await fetch('/api/v1/messages/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    chatId: selectedChatId,
                    userId: CURRENT_USER_ID,
                    text: text
                })
            });

        if (!response.ok) {
            return;
        }

        input.value = '';

        await loadMessages();

    } catch (e) {
        console.error(e);
    }
}

document.getElementById('messageInput')
    ?.addEventListener('keydown', function (e) {

        if (e.key === 'Enter') {
            sendMessage();
        }
    });

document.getElementById('chatSearch')
    ?.addEventListener('input', function () {

        const value =
            this.value.toLowerCase();

        document.querySelectorAll('.chat-card')
            .forEach(chat => {

                const name =
                    chat.dataset.chatName.toLowerCase();

                chat.style.display =
                    name.includes(value)
                        ? 'flex'
                        : 'none';
            });
    });

function openCreateChatModal() {

    const name =
        prompt('Enter chat name');

    if (!name) {
        return;
    }

    fetch('/api/v1/chats/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: name,
            userOneId: CURRENT_USER_ID
        })
    })
        .then(() => location.reload())
        .catch(console.error);
}