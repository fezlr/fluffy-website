const container = document.getElementById('feed-container');
const seed      = container.dataset.seed;
const size      = container.dataset.size;
const userId    = container.dataset.userId;

let currentPage = 0;
let isLoading   = false;
let finished    = false;

const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

const feedList  = document.getElementById('posts-list');
const spinner   = document.getElementById('spinner');
const emptyFeed = document.getElementById('empty-feed');
const sentinel  = document.getElementById('sentinel');

async function loadFeed() {
    if (isLoading || finished) return;
    isLoading = true;
    spinner.style.display = 'flex';

    try {
        const res = await fetch(`/api/v1/feed?seed=${seed}&page=${currentPage}&size=${size}`);
        if (!res.ok) throw new Error('Failed to load feed');

        const feeds = await res.json();

        if (feeds.length === 0) {
            finished = true;
            if (currentPage === 0) emptyFeed.style.display = 'block';
            observer.disconnect();
            return;
        }

        feeds.forEach(feed => feedList.insertAdjacentHTML('beforeend', renderFeed(feed)));
        currentPage++;

        if (feeds.length < size) {
            finished = true;
            observer.disconnect();
        }
    } catch (e) {
        console.error(e);
    } finally {
        isLoading = false;
        spinner.style.display = 'none';
    }
}

function renderFeed(feed) {
    const avatar = feed.avatarUrl ?? '/images/default-avatar.png';
    const imgBlock = feed.photoUrl
        ? `<img class="feed-card-img" src="${feed.photoUrl}" alt="">`
        : '';
    const textBlock = feed.text
        ? `<div class="feed-card-body"><p class="feed-card-text">${feed.text}</p></div>`
        : '';
    const actions = String(feed.userId) === String(userId) ? `
        <div class="feed-card-actions">
            <button class="btn-card-action btn-card-edit" data-id="${feed.id}"
                    data-text="${feed.text ?? ''}" data-photo="${feed.photoUrl ?? ''}">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"
                     fill="none" stroke="currentColor" stroke-width="2.5"
                     stroke-linecap="round" stroke-linejoin="round">
                    <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                    <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
            </button>
            <button class="btn-card-action btn-card-delete" data-id="${feed.id}">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24"
                     fill="none" stroke="currentColor" stroke-width="2.5"
                     stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="3 6 5 6 21 6"/>
                    <path d="M19 6l-1 14a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2L5 6"/>
                    <path d="M10 11v6M14 11v6"/>
                    <path d="M9 6V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                </svg>
            </button>
        </div>` : '';

    return `
        <div class="feed-card" data-id="${feed.id}">
            <div class="feed-card-header">
                <a href="/profiles/${feed.userId}" class="feed-card-author">
                    <img class="feed-card-avatar" src="${avatar}" alt="">
                    <span class="feed-card-name">${feed.firstName} ${feed.lastName}</span>
                </a>
                ${actions}
            </div>
            ${imgBlock}
            ${textBlock}
        </div>
    `;
}

const observer = new IntersectionObserver(entries => {
    if (entries[0].isIntersecting) loadFeed();
});
observer.observe(sentinel);

const modal      = document.getElementById('post-modal');
const btnAddPost = document.getElementById('btn-add-post');
const btnCancel  = document.getElementById('btn-cancel');
const btnSubmit  = document.getElementById('btn-submit');
const postPhoto  = document.getElementById('post-photo');
const postText   = document.getElementById('post-text');
const photoName  = document.getElementById('photo-name');
const modalError = document.getElementById('modal-error');

btnAddPost.addEventListener('click', () => modal.style.display = 'flex');
btnCancel.addEventListener('click',  closeModal);

postPhoto.addEventListener('change', () => {
    photoName.textContent = postPhoto.files[0]?.name ?? 'No file chosen';
});

btnSubmit.addEventListener('click', async () => {
    const hasPhoto = postPhoto.files.length > 0;
    const hasText  = postText.value.trim().length > 0;

    if (!hasPhoto && !hasText) {
        modalError.style.display = 'block';
        return;
    }
    modalError.style.display = 'none';
    btnSubmit.disabled = true;

    const formData = new FormData();
    formData.append('userId', userId);
    formData.append('text', postText.value.trim());
    if (hasPhoto) formData.append('photo', postPhoto.files[0]);

    try {
       const res = await fetch('/api/v1/feed/create', {
           method: 'POST',
           headers: {
           [csrfHeader]: csrfToken
           },
           body: formData
       });
       if (!res.ok) throw new Error('Failed to create post');

        const created = await res.json();
        feedList.insertAdjacentHTML('afterbegin', renderFeed(created));
        emptyFeed.style.display = 'none';
        closeModal();
    } catch (e) {
        console.error(e);
    } finally {
        btnSubmit.disabled = false;
    }
});

modal.addEventListener('click', e => { if (e.target === modal) closeModal(); });

function closeModal() {
    modal.style.display = 'none';
    postPhoto.value     = '';
    postText.value      = '';
    photoName.textContent = 'No file chosen';
    modalError.style.display = 'none';
}


const editModal        = document.getElementById('edit-modal');
const editText         = document.getElementById('edit-text');
const editPhoto        = document.getElementById('edit-photo');
const editPhotoName    = document.getElementById('edit-photo-name');
const editCurrentWrap  = document.getElementById('edit-current-photo-wrap');
const editCurrentPhoto = document.getElementById('edit-current-photo');
const editRemovePhoto  = document.getElementById('edit-remove-photo');
const btnEditCancel    = document.getElementById('btn-edit-cancel');
const btnEditSubmit    = document.getElementById('btn-edit-submit');
const editModalError   = document.getElementById('edit-modal-error');

let editingPostId = null;
let editingCurrentPhoto = null;

feedList.addEventListener('click', e => {
    const editBtn  = e.target.closest('.btn-card-edit');
    const deleteBtn = e.target.closest('.btn-card-delete');

    if (editBtn) {
        editingPostId = editBtn.dataset.id;
        editingCurrentPhoto = editBtn.dataset.photo || null;

        editText.value = editBtn.dataset.text || '';
        editRemovePhoto.checked = false;
        editPhoto.value = '';
        editPhotoName.textContent = 'Replace photo (optional)';
        editModalError.style.display = 'none';

        if (editingCurrentPhoto) {
            editCurrentPhoto.src = editingCurrentPhoto;
            editCurrentWrap.style.display = 'block';
        } else {
            editCurrentWrap.style.display = 'none';
        }

        editModal.style.display = 'flex';
    }

    if (deleteBtn) {
        deletingPostId = deleteBtn.dataset.id;
        deleteModal.style.display = 'flex';
    }
});

editPhoto.addEventListener('change', () => {
    editPhotoName.textContent = editPhoto.files[0]?.name ?? 'Replace photo (optional)';
});

btnEditCancel.addEventListener('click', () => editModal.style.display = 'none');
editModal.addEventListener('click', e => { if (e.target === editModal) editModal.style.display = 'none'; });

btnEditSubmit.addEventListener('click', async () => {
    const hasPhoto   = editPhoto.files.length > 0;
    const hasText    = editText.value.trim().length > 0;
    const keepPhoto  = editingCurrentPhoto && !editRemovePhoto.checked;

    if (!hasPhoto && !hasText && !keepPhoto) {
        editModalError.style.display = 'block';
        return;
    }
    editModalError.style.display = 'none';
    btnEditSubmit.disabled = true;

    try {
        const formData = new FormData();
        formData.append('text', editText.value.trim());
        formData.append('removePhoto', editRemovePhoto.checked);
        if (hasPhoto) formData.append('photo', editPhoto.files[0]);

        const res = await fetch(`/api/v1/feed/update/${editingPostId}`, {
            method: 'PATCH',
            headers: { [csrfHeader]: csrfToken },
            body: formData
        });
        if (!res.ok) throw new Error('Edit failed');

        const updated = await res.json();
        const card = feedList.querySelector(`.feed-card[data-id="${editingPostId}"]`);
        if (card) card.outerHTML = renderFeed(updated);

        editModal.style.display = 'none';
    } catch (e) {
        console.error(e);
    } finally {
        btnEditSubmit.disabled = false;
    }
});


const deleteModal     = document.getElementById('delete-modal');
const btnDeleteCancel = document.getElementById('btn-delete-cancel');
const btnDeleteConfirm = document.getElementById('btn-delete-confirm');
let deletingPostId = null;

btnDeleteCancel.addEventListener('click', () => deleteModal.style.display = 'none');
deleteModal.addEventListener('click', e => { if (e.target === deleteModal) deleteModal.style.display = 'none'; });

btnDeleteConfirm.addEventListener('click', async () => {
    if (!deletingPostId) return;
    btnDeleteConfirm.disabled = true;

    try {
        const res = await fetch(`/api/v1/feed/delete/${deletingPostId}`, {
            method: 'DELETE',
            headers: { [csrfHeader]: csrfToken }
        });
        if (!res.ok) throw new Error('Delete failed');

        feedList.querySelector(`.feed-card[data-id="${deletingPostId}"]`)?.remove();
        if (!feedList.children.length) emptyFeed.style.display = 'block';
        deleteModal.style.display = 'none';
    } catch (e) {
        console.error(e);
    } finally {
        btnDeleteConfirm.disabled = false;
    }
});

document.querySelectorAll('.suggestion-item').forEach(item => {
    item.addEventListener('click', (e) => {
        if (e.target.closest('.suggestion-link')) return;

        const userIdVal = item.dataset.userId;
        if (userIdVal) window.location.href = `/profiles/${userIdVal}`;
    });
});