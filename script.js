document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');
    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const email = document.querySelector('input[name="email"]').value;
        const password = document.querySelector('input[name="password"]').value;

        const adminEmail = "admin@loanservice.com";
        const adminPassword = "admin1234";

        if (email === adminEmail && password === adminPassword) {
            // Provide feedback to the user, e.g., welcome message or redirect
            alert("Welcome, Admin!");
            window.location.href = "admin-dashboard.html";  // Make sure this points to your actual dashboard
        } else {
            // Add error message for the user
            const errorMessage = document.createElement('p');
            errorMessage.textContent = "Invalid credentials. Please try again.";
            errorMessage.style.color = "red";
            form.appendChild(errorMessage);  // Display the error message on the page
        }
    });
});
