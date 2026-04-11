let sessionToken = null;

document.addEventListener("DOMContentLoaded", () => {
    if (sessionStorage.getItem("verifyPending")) {
        sessionToken = sessionStorage.getItem("token");
        document.querySelector("form:not(#verify-form)").style.display = "none";
        document.getElementById("confirm-code-form").style.display = "block";
    }
});

async function save() {
    const response = await fetch("/api/v1/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": document.getElementById("csrfToken").value
        },
        body: JSON.stringify({
            username: document.getElementById("username").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            confirmPassword: document.getElementById("confirmPassword").value
        })
    });

    if (response.ok) {
        const data = await response.json();
        sessionToken = data.token;

        sessionStorage.setItem("token", data.token);
        sessionStorage.setItem("verifyPending", "true");

        document.querySelector("form:not(#verify-form)").style.display = "none";
        document.getElementById("confirm-code-form").style.display = "block";
    } else {
        const error = await response.json();
        console.log(error);
        window.location.href = `/register?error=${encodeURIComponent(error.message)}`;
    }
}

async function confirmCode() {
    const response = await fetch("/api/v1/auth/confirm-code", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": document.getElementById("csrfToken").value
        },
        body: JSON.stringify({
            token: sessionToken,
            code: document.getElementById("code").value
        })
    });

    if (response.ok) {
        window.location.href = "/login";
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("verifyPending");
    } else {
        document.getElementById("error-message").textContent = "Invalid or expired code";
        document.getElementById("error-box").style.display = "block";
    }
}