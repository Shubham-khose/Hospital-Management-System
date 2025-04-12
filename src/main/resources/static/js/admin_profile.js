document.addEventListener("DOMContentLoaded", function() {
	const items = document.querySelectorAll("#adminSelectableList .list-group-item");

	items.forEach(item => {
		item.addEventListener("click", function() {
			console.log("clicked")
			items.forEach(i => i.classList.remove("active")); // Remove active from all
			this.classList.add("active"); // Add active to the clicked one
		});
	});
});

/*-------- js for switching one to another section ------------*/
document.addEventListener("DOMContentLoaded", function() {
	const items = document.querySelectorAll("#adminSelectableList .list-group-item");
	function showAdminDetails(detailType) {
		document.querySelectorAll(".admin_profile").forEach(section => {
			section.style.display = "none";
		});
		document.getElementById(detailType).style.display = "block";
	}

	showAdminDetails("Admin_profile");

	items.forEach(item => {
		item.addEventListener("click", function(event) {
			event.preventDefault();
			console.log(event)
			items.forEach(i => i.classList.remove("active"));
			this.classList.add("active");

			let section = this.getAttribute("data-section");
			console.log(section);
			showAdminDetails(section);
		});
	});
});

/*update Doctor Details*/

function loadUpdateDoctor(event) {
	event.preventDefault();
	let did = document.getElementById("doctorId").value;
	console.log("Clicked: ", did);
	fetch(`/updatedoctor?doctor=${did}`)
		.then(response => {
			if (!response.ok) {
				let container = document.querySelector("#doctorFoundNotFound");
				            container.innerHTML = `
				                <div class="alert text-danger" role="alert">
				                    <p class="text-center">Doctor not found please enter valid Id!!</p>
				                </div>`;
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();  // Expect JSON response
		})
		.then(data => {
			if (Object.keys(data).length === 0) {  // Check if response is empty
				console.log("No Doctor Found");
			} else {
				console.log("Doctor: ", data);

				let tbody = document.querySelector("#update_doctor_detail");
				tbody.innerHTML = `
				    <tr><th>Patient Name</th><td><input type="text" name="name" value="${data.name}"/></td></tr>
					<tr><th>Age</th><td><input type="number" name="age" value="${data.age}" style="width: 100%;"/></td></tr>
					<tr><th>Gender</th><td>
					            <select class="form-select" name="gender" aria-label="Gender">
					                <option value="${data.gender}" selected>${data.gender}</option>
					                ${data.gender !== "Male" ? '<option value="Male">Male</option>' : ''}
					                ${data.gender !== "Female" ? '<option value="Female">Female</option>' : ''}
					                ${data.gender !== "Other" ? '<option value="Other">Other</option>' : ''}
					            </select>
					        </td></tr>
							<tr><th>Specialization</th><td><input type="text" name="specialization" value="${data.specialization}" style="width: 100%;"/></td></tr>
							<tr><th>Experience</th><td><input type="number" name="experience" value="${data.experience}" style="width: 100%;"/></td></tr>
					        <tr><th>Email Id</th><td><input type="email" name="emailId" value="${data.emailId}" style="width: 100%;"/></td></tr>
					        <tr><th>Languages</th><td><input type="text" name="language" value="${data.language}" style="width: 100%;"/></td></tr>
					        <tr><th>Mobile No</th><td><input type="tel" name="mobileNo" value="${data.mobileNo}" /></td></tr>
					    
					`;

				// **Adding the hidden input field for pid**
				tbody.innerHTML += `<input type="hidden" name="dId" value="${data.did}"/>`;

				let button = document.querySelector("#updateDoctorButton");
				button.className = "bg-primary";
				button.textContent = "Update Doctor";

				let button2 = document.querySelector("#deleteDoctorButton");
				button2.className = "bg-primary";
				button2.textContent = "Remove Doctor";


			}
		})
		.catch(error => console.error("Error fetchin patient details: ", error));
}

function setAction(actionUrl) {
	document.getElementById("doctorForm").action = actionUrl;
}




/*For Records */

function fetchReport() {
    let year = document.getElementById("year").value;
    let month = document.getElementById("month").value;

    fetch(`/monthly-report?year=${year}&month=${month}`)
    .then(response => response.json())
    .then(data => {
        let table = document.getElementById("reportTable");
        table.innerHTML = `
            <tr><th>Total Patient Treated</th><td>${data.TotalPatientsTreated}</td></tr>
            <tr><th>Total Doctor Added</th><td>${data.TotalDoctorsAdded}</td></tr>
            <tr><th>Total Doctor's Removed</th><td>${data.TotalDoctorsRemoved}</td></tr>
            <tr><th>Total Appointments Taken</th><td>${data.TotalAppointmentsTaken}</td></tr>
            <tr><th>Total Appointments Cancelled</th><td>${data.TotalAppointmentsCancelled}</td></tr>
            <tr><th>Total Fee Collected</th><td>Rs. ${data.TotalFeeCollected}</td></tr>
        `;
    });
}


function loadPaymentRequests(){
	
	fetch('/payment-requests')
	.then(response => response.json())
			.then(data => {
				console.log("###########################")
				console.log(data)
				let tbody = document.querySelector("#payment_request");
				tbody.innerHTML = ""; // Clears old data before adding new
				data.forEach(appointment => {
					/*console.log(appointment);*/
					let tr = document.createElement("tr");

					let td1 = document.createElement("td");
					td1.textContent = appointment.PatientName;
					
					let td2 = document.createElement("td");
					td2.textContent = appointment.DoctorName;

					let td3 = document.createElement("td");
					td3.textContent ="₹"+appointment.Payment;
					td3.style="color:red";

					let td4 = document.createElement("td");
					td4.textContent = appointment.PayStatus;

					
					tr.appendChild(td1);
					tr.appendChild(td2);
					tr.appendChild(td3);
					tr.appendChild(td4);
					
					tbody.appendChild(tr);
				});
			})
			.catch(error => console.error("Error fetching appointment status:", error));
}