const tokenFromUrl = new URLSearchParams(window.location.search).get("token");

async function resetPassword() {
    const response = await fetch("/api/v1/auth/reset-password", {
                method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": document.getElementById("csrfToken").value
            },
            body: JSON.stringify({
               newPassword: document.getElementById("password").value,
               confirmPassword: document.getElementById("confirmPassword").value,
               token: tokenFromUrl
            })
        });

    if (response.ok) {
        window.location.href = `/reset-password-complete`;
    } else {
        const error = await response.json();
        window.location.href = `/reset-password?error=${error.status}`;
    }
}