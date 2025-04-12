// JavaScript to switch between login forms based on the selected radio button
/*const doctorRadio = document.getElementById("doctor");
const patientRadio = document.getElementById("patient");
const adminRadio = document.getElementById("admin");

const doctorLogin = document.getElementById("doctorLogin");
const patientLogin = document.getElementById("patientLogin");
const adminLogin = document.getElementById("adminLogin");

doctorRadio.addEventListener("change", function () {
  doctorLogin.style.display = "block";
  patientLogin.style.display = "none";
  adminLogin.style.display = "none";
});

patientRadio.addEventListener("change", function () {
  doctorLogin.style.display = "none";
  patientLogin.style.display = "block";
  adminLogin.style.display = "none";
});

adminRadio.addEventListener("change", function () {
  doctorLogin.style.display = "none";
  patientLogin.style.display = "none";
  adminLogin.style.display = "block";
});*/

// Function to show the correct login form
function showLoginForm(userType) {
  document.getElementById("doctorLogin").style.display = "none";
  document.getElementById("patientLogin").style.display = "none";
  document.getElementById("adminLogin").style.display = "none";

  document.getElementById(userType + "Login").style.display = "block";
}

// Get radio 	
const doctorRadio = document.getElementById("doctor");
const patientRadio = document.getElementById("patient");
const adminRadio = document.getElementById("admin");

// Check if a user type was previously selected (from localStorage)
document.addEventListener("DOMContentLoaded", function () {
  let selectedType = localStorage.getItem("selectedUserType") || "doctor";
  document.getElementById(selectedType).checked = true;
  showLoginForm(selectedType);
});

// Add event listeners to update localStorage when selection changes
doctorRadio.addEventListener("change", function () {
  localStorage.setItem("selectedUserType", "doctor");
  showLoginForm("doctor");
});

patientRadio.addEventListener("change", function () {
  localStorage.setItem("selectedUserType", "patient");
  showLoginForm("patient");
});

adminRadio.addEventListener("change", function () {
  localStorage.setItem("selectedUserType", "admin");
  showLoginForm("admin");
});


/*for Clearing error messages after reloading*/
document.addEventListener("DOMContentLoaded", function () {
    let alertMessages = document.querySelectorAll(".alert");

    alertMessages.forEach(alert => {
        if (alert) {
            setTimeout(function () {
                alert.style.display = "none";

                // Remove session message via AJAX request
                fetch('/clear-session-message', { method: 'POST' })
                    .then(response => console.log("Session message cleared"))
                    .catch(error => console.error("Error clearing session message:", error));

            }, 2000);
        }
    });
});


/*userProfile*/

