const photoPreview = document.getElementById("mainPhotoUrl");

document.addEventListener("DOMContentLoaded", () => {
    const photoInput = document.getElementById("photoInput");
    const photoPreview = document.getElementById("mainPhotoUrl");
    const saveButton = document.querySelector(".btn-save");
    const accountSaveButton = document.querySelector(".btn-save-account")

    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content || "";
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || "";
    const defaultAvatar = "/images/default-avatar.png";
    const maxFileSize = 5 * 1024 * 1024;

    let uploadedPhotoUrl = null;

    bindNavigation();
    bindPhotoInput();
    bindSave();

    function bindNavigation() {
        const items = document.querySelectorAll(".settings-item");
        const sections = document.querySelectorAll(".settings-section");

        items.forEach((item) => {
            item.addEventListener("click", () => {
                const sectionName = item.dataset.section;

                items.forEach((el) => el.classList.remove("active"));
                sections.forEach((section) => section.classList.remove("active"));

                item.classList.add("active");

                const target = document.getElementById(`section-${sectionName}`);
                if (target) target.classList.add("active");
            });
        });
    }

    function bindPhotoInput() {
        if (!photoInput || !photoPreview) return;

        photoInput.addEventListener("change", () => {
            const file = photoInput.files?.[0];
            if (!file) return;

            if (!file.type.startsWith("image/") || file.size > maxFileSize) {
                alert("Invalid file");
                photoInput.value = "";
                return;
            }

            const reader = new FileReader();
            reader.onload = () => {
                photoPreview.src = reader.result || defaultAvatar;
            };
            reader.readAsDataURL(file);
        });
    }

    function bindSave() {
        saveButton?.addEventListener("click", handleSave);
        accountSaveButton?.addEventListener("click", handleSaveAccount);
        document.getElementById("resetPasswordBtn")?.addEventListener("click", sendResetPasswordLink);
        bindAccountDirtyCheck();
        bindProfileDirtyCheck();
    }

    function bindProfileDirtyCheck() {
        const fields = ["firstName", "lastName", "birthDate", "gender", "city", "aboutMe"];

        const initial = {};
        fields.forEach(id => {
            initial[id] = document.getElementById(id)?.value || "";
        });

        const initialPhoto = photoPreview?.src || "";
        saveButton.disabled = true;

        function checkDirty() {
            const changed = fields.some(id => (document.getElementById(id)?.value || "") !== initial[id]);
            const photoChanged = photoInput?.files?.length > 0;
            saveButton.disabled = !(changed || photoChanged);
        }

        fields.forEach(id => {
            document.getElementById(id)?.addEventListener("input", checkDirty);
            document.getElementById(id)?.addEventListener("change", checkDirty);
        });

        photoInput?.addEventListener("change", checkDirty);
    }

    function bindAccountDirtyCheck() {
        const usernameInput = document.getElementById("username");
        const emailInput = document.getElementById("email");

        const initial = {
            username: usernameInput?.value || "",
            email: emailInput?.value || "",
        };

        accountSaveButton.disabled = true;

        function checkDirty() {
            const changed =
                usernameInput?.value !== initial.username ||
                emailInput?.value !== initial.email;
            accountSaveButton.disabled = !changed;
        }

        usernameInput?.addEventListener("input", checkDirty);
        emailInput?.addEventListener("input", checkDirty);
    }

   async function handleSaveAccount(e) {
       e.preventDefault();

       if (!csrfToken || !csrfHeader) {
           console.error("CSRF not found");
           return;
       }

       setAccountLoading(true);

       try {
           const usernameInput = document.getElementById("username");
           const emailInput = document.getElementById("email");

           const newUsername = usernameInput?.value || "";
           const newEmail = emailInput?.value || "";

           const initialUsername = usernameInput?.dataset.initial || "";
           const initialEmail = emailInput?.dataset.initial || "";

           const emailChanged = newEmail !== initialEmail;
           const usernameChanged = newUsername !== initialUsername;

           if (emailChanged) {
               const userId = document.getElementById("accountSection")?.dataset.userId;

               const response = await fetch(`/api/v1/auth/confirm-email/${userId}`, {
                   method: "POST",
                   headers: {
                       "Content-Type": "application/json",
                       [csrfHeader]: csrfToken
                   },
                   body: newEmail
               });

               if (!response.ok) {
                       const err = await response.json();
                       throw new Error(err.message || "Failed to send confirmation email");
               }

               alert("Confirmation email sent. Please check your inbox.");
           }

           if (usernameChanged) {
               const response = await fetch("/api/v1/user/update", {
                   method: "PATCH",
                   headers: {
                       "Content-Type": "application/json",
                       [csrfHeader]: csrfToken
                   },
                   body: JSON.stringify({ username: newUsername })
               });

               if (!response.ok) throw new Error("Failed to save username");
           }

           if (!emailChanged) {
               window.location.href = "/profiles";
           }

       } catch (err) {
           console.error(err);
           alert(err.message);
       } finally {
           setAccountLoading(false);
       }
   }

    async function handleSave(e) {
        e.preventDefault();

        if (!csrfToken || !csrfHeader) {
            console.error("CSRF not found");
            return;
        }

        setLoading(true);

        try {
            if (photoInput?.files?.length) {
                uploadedPhotoUrl = await uploadPhoto(photoInput.files[0]);
            }

            const data = {
                firstName: document.getElementById("firstName")?.value || "",
                lastName: document.getElementById("lastName")?.value || "",
                birthDate: document.getElementById("birthDate")?.value || null,
                gender: document.getElementById("gender")?.value || null,
                city: document.getElementById("city")?.value || "",
                aboutMe: document.getElementById("aboutMe")?.value || "",
                mainPhotoUrl: uploadedPhotoUrl || photoPreview.src,
            };

            const response = await fetch("/api/v1/profile/edit", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(data)
            });

            if (!response.ok) {
                throw new Error("Save failed");
            }

            alert("Profile saved");
            window.location.href = "/profiles";
        } catch (err) {
            console.error(err);
            alert("Error while saving");
        } finally {
            setLoading(false);
        }
    }

    async function uploadPhoto(file) {
        const formData = new FormData();
        formData.append("file", file);

        const response = await fetch("/api/v1/profile/upload-photo", {
            method: "POST",
            headers: {
                [csrfHeader]: csrfToken
            },
            body: formData
        });

        if (!response.ok) {
            throw new Error("Upload failed");
        }

        const data = await response.json();
        return data.url;
    }

    const userEmail = document.getElementById("accountEmail")?.dataset.userEmail;
    async function sendResetPasswordLink() {
        try {
            const response = await fetch("/api/v1/auth/send-reset-password", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            [csrfHeader]: csrfToken
                        },
                        body: JSON.stringify({
                            email: userEmail
                        })
                    });

                if (!response.ok) {
                    const error = await response.json();
                    throw new Error(error.message || "Failed to send reset link");
                }

                alert("Reset link sent to your email");
            } catch (error) {
                console.error(error);
                alert(error.message)
            }




    }


    function getValue(id) {
        return document.getElementById(id)?.value || "";
    }

    function isDefaultAvatar(src) {
        return !src || src.includes("default-avatar.png");
    }

    function setLoading(state) {
        if (saveButton) saveButton.disabled = state;
        if (photoInput) photoInput.disabled = state;
    }

    function setAccountLoading(state) {
            if (accountSaveButton) accountSaveButton.disabled = state;
        }
});