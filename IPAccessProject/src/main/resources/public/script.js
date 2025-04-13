const loginForm = document.querySelector('form');
if (loginForm) {
    loginForm.addEventListener('submit', function (event) {
        event.preventDefault();

        var email = document.querySelector('input[name="email"]').value;
        var password = document.querySelector('input[name="password"]').value;

        var adminEmail = "admin@loanservice.com";
        var adminPassword = "admin1234";

        if (email === adminEmail && password === adminPassword) {
            alert("Welcome, Admin!");
            window.location.href = "/admin-dashboard";
        } else {
            alert("Invalid credentials. Please try again.");
        }
    });
}

// handles inventory page using endpoint /IPaccess/getAllDevices
function loadDevices(onlyAvailable = false) {
    let url = '/IPaccess/getDeviceInventory';
    if (onlyAvailable) {
        url += '?available=true';
    }

    fetch(url)
        .then(res => res.json())
        .then(devices => {
            const tbody = document.querySelector('#deviceTable tbody');
            if (!tbody) return;

            tbody.innerHTML = '';
            devices.forEach(device => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${device.deviceID}</td>
                    <td>${device.deviceType}</td>
                    <td>${device.deviceStatus}</td>
                    <td>${device.availability ? 'Yes' : 'No'}</td>
                    <td>${device.locationName}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => {
            console.error('Error fetching device inventory:', err);
        });
}


document.addEventListener('DOMContentLoaded', () => {
    const availabilitySelect = document.getElementById('availability');
    if (availabilitySelect) {
        availabilitySelect.addEventListener('change', function () {
            const showAvailableOnly = this.value === 'available';
            loadDevices(showAvailableOnly);
        });

        loadDevices();
    }
});

// Works very similar to loadDevices
function loadLoans() {
    fetch('/IPaccess/getAllLoans')
        .then(res => res.json())
        .then(loans => {
            const tbody = document.querySelector('#loanTable tbody');
            if (!tbody) return;

            tbody.innerHTML = '';
            loans.forEach(loan => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${loan.loanID}</td>
                    <td>${loan.userID}</td>
                    <td>${loan.deviceID}</td>
                    <td>${loan.startDate}</td>
                    <td>${loan.endDate}</td>
                    <td>${loan.loanStatus}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => {
            console.error('Error fetching loans:', err);
        });
}

document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('loanTable')) {
        loadLoans();
    }
});
