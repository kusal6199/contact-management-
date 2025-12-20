const imageInput = document.querySelector("#image_file_input");
const previewImg = document.getElementById("upload_image_preview");

imageInput.addEventListener("change", function (event) {
  const file = event.target.files[0];

  // No file selected
  if (!file) {
    previewImg.src = "";
    return;
  }

  // Validate file type
  if (!file.type.startsWith("image/")) {
    alert("Only image files are allowed!");
    imageInput.value = ""; // reset input
    previewImg.src = "";
    return;
  }

  // Validate file size (2MB)
  const MAX_SIZE = 2 * 1024 * 1024;
  if (file.size > MAX_SIZE) {
    alert("Image size must be less than 2MB");
    imageInput.value = "";
    previewImg.src = "";
    return;
  }

  // Preview image
  const reader = new FileReader();
  reader.onload = function () {
    previewImg.src = reader.result;
  };
  reader.readAsDataURL(file);
});
