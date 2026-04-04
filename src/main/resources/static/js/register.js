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
            password: document.getElementById("password").value
        })
    });

    if (response.ok) {
        alert("Code has been sent to your email");
    } else {
          const error = await response.json();
          console.log(error);
          window.location.href = `/register?error=${error.status}`;
      }
}