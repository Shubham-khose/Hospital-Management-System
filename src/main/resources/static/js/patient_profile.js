document.addEventListener("DOMContentLoaded", function() {
	const items = document.querySelectorAll("#selectableList .list-group-item");

	items.forEach(item => {
		item.addEventListener("click", function() {
			items.forEach(i => i.classList.remove("active")); // Remove active from all
			this.classList.add("active"); // Add active to the clicked one
		});
	});
});


/*-------- js for switching one to another section ------------*/
document.addEventListener("DOMContentLoaded", function() {
	const items = document.querySelectorAll("#selectableList .list-group-item");
	// const backButton = document.querySelector(".btn-outline-primary"); // Select the back button
	// let previousSection = "patient_profile"; // Store last visited section

	// Function to show selected section and hide others
	function showPatientDetail(detailType) {
		document.querySelectorAll(".p_profile").forEach(section => {
			/*profile is in div of sections of details not in list */
			section.style.display = "none";
		});

		document.getElementById(detailType).style.display = "block";
		// previousSection = detailType; // Update the last visited section
	}

	// Show default section ("patient_profile") on page load
	showPatientDetail("patient_profile");

	// Event listeners for list items
	items.forEach(item => {
		item.addEventListener("click", function(event) {
			event.preventDefault(); // Prevent default anchor behavior

			// Remove active class from all items
			items.forEach(i => i.classList.remove("active"));
			this.classList.add("active"); // Highlight selected item

			// Get the section ID from data attribute
			let section = this.getAttribute("data-section");
			showPatientDetail(section); // Show selected section
		});
	});

	// Back Button Functionality
	/*backButton.addEventListener("click", function () {
		showPatientDetail("patient_profile"); // Always go back to profile
	});

	// Auto-set today's date in the date picker
	const dateInput = document.querySelector('input[type="date"]');
	if (dateInput) {
		dateInput.valueAsDate = new Date();
	}*/
});
/**********************************************************************************************/


/*get specialization*/
document.addEventListener("DOMContentLoaded", function() {
	fetch('/patient/specialization')  /*The fetch() function sends a request to the backend endpoint /patient/specialization. */
		.then(response => response.json()) /*response.json() converts the response body into a JavaScript object (from JSON format).*/
		.then(data => { /*			data now contains the list of specializations retrieved from the backend.
			We use this data to dynamically populate the dropdown list.*/
			let dropdown = document.getElementById("specializationDropdown");
			data.forEach(specialization => {
				let option = document.createElement("option");  /*Creates a new <option> element for the dropdown.*/
				option.value = specialization;/*Sets the value of the option (used when submitting a form).*/
				option.textContent = specialization;/*Sets the display text that users see.*/
				dropdown.appendChild(option);/*Adds the newly created <option> to the dropdown menu.*/
			});
		})
		.catch(error => console.error("Error fetching specializations:", error));
	/*			Catches any errors if the request fails (e.g., server error, network issue).
	Logs an error message to the console for debugging. */
});

/**********************************************************************************************/

/*get Doctors By Specialization*/

function loadDoctors() {
	let specialization = document.getElementById("specializationDropdown").value;
	let doctorDropdown = document.getElementById("doctorDropdown");

	if (specialization) {
		fetch(`/patient/getDoctorsBySpecialization?specialization=${specialization}`)
			.then(response => response.json())
			.then(data => {
				/*doctorDropdown.innerHTML = '<option selected>Choose Doctor...</option>';*/
				data.forEach(doctor => {
					let option = document.createElement("option");
					option.value = `${doctor.id}|${doctor.name}`;
					// first check which attribute is conme in object then get that attribute to access value like id (rather than) dId
					option.textContent = `${doctor.name}`;
					doctorDropdown.appendChild(option);
				});
			})
			.catch(error => console.error("Error fetching doctors:", error));
	} else {
		doctorDropdown.innerHTML = '<option selected>Choose Doctor...</option>';
	}
}
/**********************************************************************************************/
/*Load fees */
function loadFees() {
	let doctorDropdown = document.getElementById("doctorDropdown");
	let selectedOption = doctorDropdown.options[doctorDropdown.selectedIndex]; // Get selected option
	let doctorFees = document.getElementById("doctorFeeValue"); // get hidden input
	let feesDisplay = document.getElementById("doctorFeeDisplay");//get visible fee element

	if (selectedOption && selectedOption.value) {
		let selectedValue = selectedOption.value.trim(); // Ensure no extra spaces
		let [doctorId, doctorName] = selectedValue.split("|"); // Extract ID and Name
		fetch(`/patient/getDoctorFee?doctorId=${doctorId}`) // Fetch doctor's fee
			.then(response => response.json())
			.then(doctor => {
				/*doctorFees.value = "fees"+doctor;*/
				doctorFees.value = doctor; //update hidden input value
				feesDisplay.innerText = `₹${doctor}`; // update visible fees 
			})
			.catch(error => console.error("Error fetching doctor fee:", error));
	} else {
		doctorFees.value = "0";//reset hidden input value
		feesDisplay.innerText = "₹0"; // Reset Displayed Fee
	}
}

/**********************************************************************************************/

// get Appointment by id 
function loadAppointment(event) {
	event.preventDefault(); // Prevents page jump due to <a href="#">
	console.log("Clicked");

	fetch('/patient/appointmentstatus')
		.then(response => response.json())
		.then(data => {
			let tbody = document.querySelector("#ap_data");
			tbody.innerHTML = ""; // Clears old data before adding new
			data.forEach(appointment => {
				/*console.log(appointment);*/
				let tr = document.createElement("tr");

				/*let td1 = document.createElement("td");
				td1.textContent = appointment.appointmentId;
*/
				let td2 = document.createElement("td");
				td2.textContent = appointment.doctorName;

				let td3 = document.createElement("td");
				td3.textContent = appointment.specialization;

				let td4 = document.createElement("td");
				td4.textContent = appointment.consultantFee;

				let td5 = document.createElement("td");
				td5.textContent = appointment.time;

				let td6 = document.createElement("td");
				td6.textContent = appointment.date;

				let td7 = document.createElement("td");
				td7.textContent = appointment.payment;
				td7.style = "color: red; font-weight: bold;";

				let td8 = document.createElement("td");
				td8.textContent = appointment.appointmentStatus;

				let td9 = document.createElement("td");
				td9.innerHTML = `<button type="submit" class="bg-primary" onclick="cancelAppointment(event, ${appointment.appointmentId})">Cancel</button>`;


				/*tr.appendChild(td1);*/
				tr.appendChild(td2);
				tr.appendChild(td3);
				tr.appendChild(td4);
				tr.appendChild(td5);
				tr.appendChild(td6);
				tr.appendChild(td7);
				tr.appendChild(td8);
				tr.appendChild(td9);


				tbody.appendChild(tr);
			});
		})
		.catch(error => console.error("Error fetching appointment status:", error));
}

/*for add Status CANCELLED */
function cancelAppointment(event, appointmentId) {
	event.preventDefault();  // Prevent default button behavior

	fetch(`/appointment/appointmentstatus-cancel`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ appointmentId: appointmentId })
	})
		.then(response => response.json())
		.then(data => {
			console.log("================", data);
			if (data.success) {
				alert("Appointment Cancelled successfully!");
				location.reload();  // Refresh page to show updated status
			} else {
				alert("Error admitting patient.");
			}
		})
		.catch(error => console.error("Error:", error));
}

/**********************************************************************************************/


/*Load All Doctors */

function loadDoctorsDetail(event) {
	event.preventDefault(); // Prevents page jump due to <a href="#">
	/*   console.log("Clicked");
   */
	fetch('/patient/getalldoctors')
		.then(response => response.json()) // Get raw response before parsing
		/* .then(text => {
			  console.log("Raw API Response:", text); 
			 return JSON.parse(text); // Manually parse JSON
		 })*/  /*this is help full for foreign key data because that data is in nested from we using criteria cannot support them */
		.then(data => {
			/*console.log("Received data:", data);*/
			let tbody = document.querySelector("#Doctor_details");
			tbody.innerHTML = ""; // Clears old data before adding new

			data.forEach(doctor => {
				/*  console.log(doctor);*/
				let tr = document.createElement("tr");

				let td1 = document.createElement("td");
				td1.textContent = doctor.Name;

				let td2 = document.createElement("td");
				td2.textContent = doctor.Specialization;

				let td3 = document.createElement("td");
				td3.textContent = "₹" + doctor.ConsultantFees;
				td3.style = "color:red;font-weight:bold;";

				tr.appendChild(td1);
				tr.appendChild(td2);
				tr.appendChild(td3);

				tbody.appendChild(tr);
			}); // Missing closing `)`
		})
		.catch(error => console.error("Error fetching doctor details:", error));
}
/**********************************************************************************************/

/*Update Patient*/

function loadUpdatePatient(event) {
	fetch("/patient/updatepatient")
		.then(response => response.json())
		.then(data => {
			let tbody = document.querySelector("#update_patient_detail");
			tbody.innerHTML = `
			<tr><th>Name</th><td><input type="text" name="name" value="${data.name}" style="width: 100%;"/></td></tr>
			    <tr><th>DOB</th><td><input type="date" name="dob" value="${data.dob}" /></td></tr>
			    <tr><th>Gender</th><td>
			        <select class="form-select" name="gender" aria-label="Gender">
			            <option value="${data.gender}" selected>${data.gender}</option>
			            <option value="Male">Male</option>
			            <option value="Female">Female</option>
			            <option value="Other">Other</option>
			        </select>
			    </td></tr>
			    <tr><th>Blood Group</th><td>                            
			        <select class="form-select" id="blood-group" name="bloodGroup" aria-label="Blood Group">
			            <option value="${data.bloodGroup}" selected>${data.bloodGroup}</option>
			            <option value="A+">A+</option>
			            <option value="A-">A-</option>
			            <option value="B+">B+</option>
			            <option value="B-">B-</option>
			            <option value="AB+">AB+</option>
			            <option value="AB-">AB-</option>
			            <option value="O+">O+</option>
			            <option value="O-">O-</option>
			        </select>
			    </td></tr>
			    <tr><th>Email Id</th><td><input type="text" name="emailId" value="${data.emailId}" style="width: 100%;"/></td></tr>
			    <tr><th>Address</th><td><input type="text" name="address" value="${data.address}" style="width: 100%;"/></td></tr>
			    <tr><th>Mobile No</th><td><input type="tel" name="mobileNo" value="${data.mobileNo}" /></td></tr>
			`;

			tbody.innerHTML += `<input type="hidden" name="pId" value="${data.pid}"/>`;
			let button = document.querySelector("#updatePatient")
			button.className = "bg-primary";
			button.textContent = "Update Patient";

		})
		.catch(error => console.error("Error fetchin patient details: ", error));
}


/******************************************************************************************/

/*Online payment Integration*/
const paymentStart = () => {

	let amount = document.querySelector("#doctorFeeValue").value;
	console.log(amount)
	if (amount == "" || amount == null) {
		/*alert("amount is required ")*/
		Swal.fire({
			title: "Failed!",
			text: "amount is required !",
			icon: "error"
		});
		return
	}

	//code...
	//we will use ajax to send request to server to create order 

	$.ajax(
		{
			url: '/patient/create_order',
			data: JSON.stringify({ amount: amount, info: 'order_request' }),
			contentType: 'application/json',
			type: 'POST',
			dataType: 'json',
			success: function(response) {
				//invoked when success
				console.log(response)
				if (response.status = 'created') {
					//Open payment form
					let options = {
					key: 'rzp_test_yv9samGMeSJty0',
						amount: response.amount,
						currency: 'INR',
						name: 'Hospital Managment System',
						description: 'Appointment',
						/*image:''*/
						order_id: response.id,
						handler: function(response) {
							console.log(response.razorpay_payment_id)
							console.log(response.razorpay_order_id)
							console.log(response.razorpay_signature)
							console.log("payment successfull")
							/*alert("Congratulations !! payment successfull")*/
							/*for sweet alert of successful payment*/

							Swal.fire({
								title: "Good job!",
								text: "Congratulations !! payment successfull!",
								icon: "success"
							}).then(() => {
									// Only after user clicks OK in SweetAlert, submit the form
									document.getElementById("appointmentForm").submit();
								});
						},
						"prefill": { //We recommend using the prefill parameter to auto-fill customer's contact information, especially their phone number
							"name": "", //your customer's name
							"email": "",
							"contact": ""  //Provide the customer's phone number for better conversion rates 
						},
						"notes": {
							"address": "Razorpay Corporate Office"
						},
						"theme": {
							"color": "#3399cc"
						}
					};

					let rzp = new Razorpay(options);
					rzp.on("payment.failed", function(response) {
						alert(response.error.code);
						console.log(response.error.description);
						console.log(response.error.source);
						console.log(response.error.step);
						console.log(response.error.reason);
						console.log(response.error.metadata.order_id);
						console.log(response.error.metadata.payment_id);
						/*alert("Oops payment failed!!")*/
						/*for sweet alert of failed payment*/

						Swal.fire({
							title: "Failed!",
							text: "Oops payment failed!",
							icon: "error"
						});
					});
					rzp.open();

				}
			},
			error: function(error) {
				//invoked when error
				console.log(error)
				alert("Something went wrong !!")
			},

		}
	)

}


/******************************************************************************************/

/*Payment Receipt Of Appointment*/
/*FRONT-END*/
function printReceipt(receiptId) {
   const printContents = document.getElementById('receiptId').innerHTML;
   const originalContents = document.body.innerHTML;

   document.body.innerHTML = printContents;
   window.print();
   document.body.innerHTML = originalContents;
   location.reload(); // reload to restore events/styles
 }
 
 
 /*BACK-END*/
 function fetchReceipt() {
     fetch("/patient/receipt-of-payment")
         .then(response => response.json())
         .then(data => {
			let receipt_container = document.getElementById("receipt_container");
			console.log(data)
			console.log(data.receiptNo)
			receipt_container.innerHTML=`
			<div id="receipt_container">
			<div id="receipt_${data.receiptNo}" class="receipt"
							style="margin: auto; padding: 20px; border: 1px solid #ccc; font-family: Arial;">
							<h3 style="text-align: center;">Payment Receipt</h3>
							<hr>
							 <p><strong>Receipt No:</strong> <span>${data.receiptNo}</span></p>
						        <p><strong>Date:</strong> <span>${data.date}</span></p>
						        <p><strong>Name:</strong> <span>${data.name}</span></p>
						        <p><strong>Method:</strong> <span>${data.method}</span></p>
						        <p><strong>Amount:</strong> ₹ <span>${data.amount}</span></p>
							<hr>
							<p style="text-align: center;">Thank you!</p>
						</div>
				
						<div class="text-center">
						<button class="btn btn-success" onclick="printReceipt('receipt_${data.receiptNo}')">Print Receipt</button>
			</div>
			</div>
			`
             document.getElementById("receiptNo").textContent = "#" + data.receiptNo;
             document.getElementById("date").textContent = data.date;
             document.getElementById("name").textContent = data.name;
             document.getElementById("method").textContent = data.method;
             document.getElementById("amount").textContent = data.amount;
         })
         .catch(error => {
             console.error("Error fetching receipt:", error);
         });
 }