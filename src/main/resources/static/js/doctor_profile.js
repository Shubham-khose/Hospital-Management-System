document.addEventListener("DOMContentLoaded", function() {
	const items = document.querySelectorAll("#doctorSelectableList .list-group-item");

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
	const items = document.querySelectorAll("#doctorSelectableList .list-group-item");
	function showDoctorDetails(detailType) {
		document.querySelectorAll(".d_profile").forEach(section => {
			section.style.display = "none";
		});
		document.getElementById(detailType).style.display = "block";
	}

	showDoctorDetails("Doctor_profile");

	items.forEach(item => {
		item.addEventListener("click", function(event) {
			event.preventDefault();
			console.log(event)
			items.forEach(i => i.classList.remove("active"));
			this.classList.add("active");

			let section = this.getAttribute("data-section");
			showDoctorDetails(section);
		});
	});
});

/*load id in prescription */
/*function loadPrescription(event) {
	event.preventDefault();
	let preId = document.querySelector("#prescriptionId").value;
	console.log("Clicked", preId)
	fetch(`/doctor/search-prescription?patient=${preId}`)
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.text();  // Read response as text
		})
		.then(text => {
			if (text.trim() === "") {
				console.log(`${patientStatus}`);
				fetch(`/patient/getPatient-description?pId=${preId}`)
					.then(response => {
						if (!response.ok) {
							throw new Error(`HTTP error! Status: ${response.status}`);
						}
						return response.json();
					})
					.then(data => {
						console.log("Patient Data:", data);
					})
					.catch(error => console.error("Error fetching patient description:", error));

				let tbody = document.querySelector("#prescription_detail");
				tbody.innerHTML = "";
				let tr1 = document.createElement("tr");
				let th1 = document.createElement("th");
				let tr2 = document.createElement("tr");
				let th2 = document.createElement("th");
				let tr3 = document.createElement("tr");
				let th3 = document.createElement("th");
				th1.textContent = "Patient Name";
				let td1 = document.createElement("td");
				td1.innerHTML = `<input type="text" name=""/>`;
				
				
				
				
				tr1.appendChild(th1);
				tr1.appendChild(td1);

				tbody.appendChild(tr1);
			}
			return JSON.parse(text); // Convert to JSON safely
		})
		.then(data => {
			console.log("inside", data);
			console.log("pID", data.pId);
		})
		.catch(error => console.error("Error fetching prescription:", error));

}*/

/*function loadPrescription(event) {
	event.preventDefault();
	let preId = document.querySelector("#prescriptionId").value;
	console.log("Clicked", preId);

	fetch(`/doctor/search-prescription?patient=${preId}`)
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.text();  // Read response as text
		})
		.then(text => {
			if (text.trim() === "") {
				console.log("No prescription found, fetching patient details...");
				
				fetch(`/doctor/getPatient-description?pId=${preId}`)
					.then(response => {
						if (!response.ok) {
							throw new Error(`HTTP error! Status: ${response.status}`);
						}
						return response.json();
					})
					.then(data => {
						console.log("Patient Data:",);

						// Display patient details
						let tbody = document.querySelector("#prescription_detail");
						tbody.innerHTML = "";
						
						let tr1 = document.createElement("tr");
						let th1 = document.createElement("th");
						let tr2 = document.createElement("tr");
						let th2 = document.createElement("th");
						let tr3 = document.createElement("tr");
						let th3 = document.createElement("th");
						let tr4 = document.createElement("tr");
						let th4 = document.createElement("th");
						let tr5 = document.createElement("tr");
						let th5 = document.createElement("th");
						th1.textContent = "Patient Name";
						let td1 = document.createElement("td");
						td1.innerHTML = `<input type="text" name="patientName" value="${data.patientName}"/>`;

						th2.textContent = "Treatment Name";
						let td2 = document.createElement("td");
						td2.innerHTML = `<input type="text" name="treatment" />`;

						th3.textContent = "Medecine";
						let td3 = document.createElement("td");
						td3.innerHTML = `<input type="text" name="medicine"/>`;

						th4.textContent = "Remark";
						let td4 = document.createElement("td");
						td4.innerHTML = `<input type="text" name="remark"/>`;

						th5.textContent = "Advice";
						let td5 = document.createElement("td");
						td5.innerHTML = `<textarea name="advice" rows="4" cols="50"></textarea>`;

							

						tr1.appendChild(th1);
						tr1.appendChild(td1);
						tr2.appendChild(th2);
						tr2.appendChild(td2);
						tr3.appendChild(th3);
						tr3.appendChild(td3);
						tr4.appendChild(th4);
						tr4.appendChild(td4);
						tr5.appendChild(th5);
						tr5.appendChild(td5);
						tbody.appendChild(tr1);
						tbody.appendChild(tr2);
						tbody.appendChild(tr3);
						tbody.appendChild(tr4);
						tbody.appendChild(tr5);
					let button = document.querySelector("#addPrescription");
					button.className="bg-primary";
					button.textContent="add Prescription";
					})
					.catch(error => console.error("Error fetching patient description:", error));

				// Stop further execution
				return;
			}

			// Parse the valid JSON response
			return JSON.parse(text);
		})
		.then(data => {
		
				console.log("inside", data);
				console.log("pID", data.pId);
		
		})
		.catch(error => console.error("Error fetching prescription:", error));
}
*/



function loadPrescription(event) {
	event.preventDefault();
	let preId = document.querySelector("#prescriptionId").value;
	console.log("Clicked", preId);

	fetch(`/doctor/search-prescription?patient=${preId}`)
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! Status: ${response.status}`);
			}
			return response.json();  // Expect JSON response
		})
		.then(data => {
			console.log("Inside loop");
			if (Object.keys(data).length === 0) {  // Check if response is empty
				console.log("No prescription found, fetching patient details...");

				fetch(`/doctor/getPatient-description?pId=${preId}`)
					.then(response => {
						if (!response.ok) {
							throw new Error(`HTTP error! Status: ${response.status}`);
						}
						return response.json();
					})
					.then(patientData => {
						console.log("Patient Data:", patientData);

						// Display patient details in a form
						let tbody = document.querySelector("#prescription_detail");
						tbody.innerHTML = "";

						let fields = [
							{ label: "Patient Name", name: "patientName", value: patientData.patientName },
							{ label: "Treatment Name", name: "treatment", value: "" },
							{ label: "Medicine", name: "medicine", value: "" },
							{ label: "Remark", name: "remark", value: "" },
							{ label: "Advice", name: "advice", value: "", textarea: true }
						];

						fields.forEach(field => {
							let tr = document.createElement("tr");
							let th = document.createElement("th");
							th.textContent = field.label;
							let td = document.createElement("td");
							td.innerHTML = field.textarea
								? `<textarea name="${field.name}" rows="4" cols="50">${field.value}</textarea>`
								: `<input type="text" name="${field.name}" value="${field.value}" />`;
							tr.appendChild(th);
							tr.appendChild(td);
							tbody.appendChild(tr);
						});

						let button = document.querySelector("#addPrescription");
						button.className = "bg-primary";
						button.textContent = "Add Prescription";
					})
					.catch(error => console.error("Error fetching patient description:", error));
			} else {
				// Prescription exists, show it
				console.log("Prescription found:", data);

				let tbody = document.querySelector("#update_prescription_detail");
				tbody.innerHTML = `
				    <tr><th>Patient Name</th><td><input type="text" name="patientName" value="${data.patientName}" readonly/></td></tr>
				    <tr><th>Treatment Name</th><td><input type="text" name="treatment" value="${data.treatmentName}"/></td></tr>
				    <tr><th>Medicine</th><td><input type="text" name="medicine" value="${data.medicine}"/></td></tr>
				    <tr><th>Remark</th><td><input type="text" name="remark" value="${data.remark}"/></td></tr>
				    <tr><th>Advice</th><td><textarea name="advice" rows="4" cols="50">${data.advice}</textarea></td></tr>
				`;

				// **Adding the hidden input field for pid**
				tbody.innerHTML += `<input type="hidden" name="pid" value="${data.patientId}"/>`;
				tbody.innerHTML += `<input type="hidden" name="preId" value="${data.prescriptionId}"/>`;

				let button = document.querySelector("#updatePrescription");
				button.className = "bg-primary";
				button.textContent = "Update Prescription";


			}
		})
		.catch(error => console.error("Error fetching prescription:", error));
}

/*get Doctor Appointments */
// get Appointment by id 
function loadDoctorAppointments(event) {
	event.preventDefault(); // Prevents page jump due to <a href="#">
	/*console.log("Clicked");*/

	fetch('/doctor/doctor-appointments')
		.then(response => response.json())
		.then(data => {
			let tbody = document.querySelector("#doctor_ap_data");
			tbody.innerHTML = ""; // Clears old data before adding new
			data.forEach(appointment => {
				/*console.log(appointment);*/
				let tr = document.createElement("tr");


				let td1 = document.createElement("td");
				td1.textContent = appointment.pId;

				let td2 = document.createElement("td");
				td2.textContent = appointment.patientName;

				let td3 = document.createElement("td");
				td3.textContent = appointment.date;

				let td4 = document.createElement("td");
				td4.textContent = appointment.time;
				tr.appendChild(td1);
				tr.appendChild(td2);
				tr.appendChild(td3);
				tr.appendChild(td4);

				if (appointment.status != null) {
				    let td5 = document.createElement("td");
				    td5.textContent = appointment.status;
				    tr.appendChild(td5);
				} else {
				    let td5 = document.createElement("td");
				    td5.innerHTML = `<button type="submit" class="bg-prmary" onclick="addAdmitDate(event, ${appointment.pId})">Admit</button>`;
				    tr.appendChild(td5);
				}

				tbody.appendChild(tr);
			});
		})
		.catch(error => console.error("Error fetching appointment status:", error));
}

function addAdmitDate(event, patientId) {
    event.preventDefault();  // Prevent default button behavior

    fetch(`/doctor/doctor-patient-admit`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ patientId: patientId })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert("Patient admitted successfully!");
            location.reload();  // Refresh page to show updated status
        } else {
            alert("Error admitting patient.");
        }
    })
    .catch(error => console.error("Error:", error));
}
