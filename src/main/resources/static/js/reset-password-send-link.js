async function sendResetPasswordLink() {
    const response = await fetch("/api/v1/auth/send-reset-password", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "X-CSRF-TOKEN": document.getElementById("csrfToken").value
            },
            body: JSON.stringify({
                email: document.getElementById("email").value
            })
        });

    if (response.ok) {
        alert("Message has been successfully sent to your email")
    } else {
        const error = await response.json();
        window.location.href = `/register?error=${error.status}`;
    }
}