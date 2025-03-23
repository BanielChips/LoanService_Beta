document.querySelector('form').addEventListener('submit', function(event) {
    event.preventDefault();  // Prevent form submission for demonstration

    var email = document.querySelector('input[name="email"]').value;
    var password = document.querySelector('input[name="password"]').value;

    // Admin credentials (hardcoded for demo)
    var adminEmail = "admin@loanservice.com";
    var adminPassword = "admin1234";

    // Check if the entered credentials match the admin credentials
    if (email === adminEmail && password === adminPassword) {
        alert("Welcome, Admin!");
        // Redirect to the admin dashboard (simulated)
        window.location.href = "/admin-dashboard";  // Change to your actual dashboard URL
    } else {
        alert("Invalid credentials. Please try again.");
    }
});
