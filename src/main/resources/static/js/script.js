console.log("Script loaded");

document.addEventListener("DOMContentLoaded", () => {
  // Wait for navbar to load (because it's loaded via Thymeleaf fragment)
  const checkButton = setInterval(() => {
    const themeButton = document.getElementById("theme_change_button");
    if (themeButton) {
      clearInterval(checkButton);
      setupThemeToggle(themeButton);
    }
  }, 200);
});

function setupThemeToggle(themeButton) {
  console.log("Theme button found");

  const html = document.documentElement; // selects <html>
  let currentTheme = localStorage.getItem("theme") || "light";

  // Apply saved theme
  html.classList.remove("light", "dark");
  html.classList.add(currentTheme);
  updateButtonText(themeButton, currentTheme);

  themeButton.addEventListener("click", () => {
    const newTheme = currentTheme === "light" ? "dark" : "light";
    html.classList.remove(currentTheme);
    html.classList.add(newTheme);
    localStorage.setItem("theme", newTheme);
    currentTheme = newTheme;
    updateButtonText(themeButton, newTheme);
    console.log("Theme changed to:", newTheme);
  });
}

function updateButtonText(button, theme) {
  const span = button.querySelector("span");
  if (span) span.textContent = theme === "light" ? "Dark" : "Light";
}
