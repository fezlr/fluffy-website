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
    return `
        <div class="feed-card">
            <div class="feed-card-header">
                <a href="/profiles/${feed.userId}" class="feed-card-author">
                    <img class="feed-card-avatar" src="${avatar}" alt="">
                    <span class="feed-card-name">${feed.firstName} ${feed.lastName}</span>
                </a>
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
       const res = await fetch('/api/v1/feed', {
           method: 'POST',
           headers: { [csrfHeader]: csrfToken },
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