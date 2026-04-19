const csrfToken = document.getElementById('csrf').value;

document.getElementById('submit-btn').addEventListener('click', async function(e) {
    e.preventDefault();

    const firstNameValue = document.getElementById('firstName').value.trim();
    const lastNameValue  = document.getElementById('lastName').value.trim();
    let valid = true;

    ['firstName', 'lastName'].forEach(id => {
        const input = document.getElementById(id);
        const error = document.getElementById(id + '-error');
        if (!input.value.trim()) {
            input.classList.add('error');
            error.classList.add('visible');
            valid = false;
        } else {
            input.classList.remove('error');
            error.classList.remove('visible');
        }
    });

    if (!valid) return;

    const response = await fetch('/api/v1/profile/setup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify({
                    firstName: firstNameValue,
                    lastName: lastNameValue
           })
    });

    if (response.ok) {
        window.location.href = '/profile';
    } else {
        document.getElementById('error-box').style.display = 'block';
    }
});

document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', () => {
        input.classList.remove('error');
        const error = document.getElementById(input.id + '-error');
        if (error) error.classList.remove('visible');
    });
});