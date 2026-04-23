const photoPreview = document.getElementById("mainPhotoUrl");

document.addEventListener("DOMContentLoaded", () => {
    const photoInput = document.getElementById("photoInput");
    const photoPreview = document.getElementById("mainPhotoUrl");
    const saveButton = document.querySelector(".btn-save");

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
        if (!saveButton)
            return;

        saveButton.addEventListener("click", handleSave);
    }

    async function handleSave(e) {
        e.preventDefault();

        if (!csrfToken || !csrfHeader) {
            console.error("CSRF not found");
            return;
        }

        if (!photoPreview || isDefaultAvatar(photoPreview.src)) {
            alert("Select a photo");
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
});