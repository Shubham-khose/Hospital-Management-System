package com.hospital.management.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.razorpay.*;
import org.hibernate.internal.build.AllowSysOut;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Doctor;
import com.hospital.management.entities.Doctor.Status;
import com.hospital.management.entities.Appointment.AppointmentStatus;
import com.hospital.management.entities.Patient;
import com.hospital.management.healper.Message;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.services.AppointmentService;
import com.hospital.management.services.AppointmentServiceImpl;
import com.hospital.management.services.DoctorService;
import com.hospital.management.services.PatientServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private PatientServices patientService;

	@Autowired
	private DoctorService doctorService;
	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	/* Patient/Doctor/Admin Login */
	@PostMapping("/patient_log")
	public String patientLogin(@RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
			@RequestParam("emailId") String email, @RequestParam("password") String password, Model model,
			HttpServletRequest request) {

		try {
			request.removeAttribute("message"); // Remove session message
			HttpSession session = request.getSession(false); // Get existing session
			if (session != null) {
				session.invalidate(); // Destroy old session
			}

			session = request.getSession(true); // Create a fresh session
			Optional<Patient> patientOptional = patientService.validatePatient(email, password);

			if (patientOptional.isPresent()) {
				Patient patient = patientOptional.get();
				session.setAttribute("loggedInPatient", patient);
				return "redirect:/patient/patientprofile"; // Redirect to profile page
			} else {
				model.addAttribute("errormessage", "Invalid Email or Password!!");
				request.getSession().setAttribute("message",
						new Message("Invalid Email or Password!!", "alert-danger"));
				return "login_pages"; // Redirect back to login
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("message",
					new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
			return "login_pages";
		}
	}

	/* Patient Add/Delete */
	@PostMapping("/add_patient")
	public String addPatient(@ModelAttribute("patient") Patient patient, BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
		try {
			if (!agreement) {
				System.out.println("You have not agreed the terms and conditions ");
				throw new Exception("You have not agreed the terms and conditions ");
			}

			if (bindingResult.hasErrors()) {
				System.out.println("ERROR" + bindingResult.toString());
				model.addAttribute("patient", patient);
			}

			System.out.println("patient: " + patient);
			Patient result = this.patientService.savePatient(patient);
			model.addAttribute("patient", new Patient());
			session.setAttribute("message", new Message("Successfully Registered!!!", "alert-success"));
			return "login_pages"; // Redirect to patient list
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("patient", patient);
			session.setAttribute("message", new Message("Something went wrong " + e.getMessage(), "alert-danger"));
			return "registration";
		}

	}

	/******************************************************************************************/
	/* Patient Profile */
	@GetMapping("/patientprofile")
	public String getPatientProfile(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false); // Get session without creating a new one

		if (session == null || session.getAttribute("loggedInPatient") == null) {
			System.out.println("No logged-in patient found in session.");
			return "redirect:/log"; // Redirect if not logged in
		}

		Patient p1 = (Patient) session.getAttribute("loggedInPatient");
		// List<String> tableHeaders = patientService.getPatientHeaders();

		// for access in appointment as foreign key
		session.setAttribute("patientId", p1.getpId());
		System.out.println("patientId " + p1.getpId());
		model.addAttribute("patient_detail", p1);
		// model.addAttribute("tableHeaders", tableHeaders);

		return "patient_home_page"; // Return the profile page
	}

	/*--------------------------------------------------------------------------*/
	/* Specialization */

	@GetMapping("/specialization")
	@ResponseBody // Returns JSON instead of a view
	public List<String> getSpecialization() {
		Status status = Status.ACTIVE;
		return doctorRepository.findDistinctSpecialization(status);
	}

	/* Get doctor by Specialization */

	@GetMapping("/getDoctorsBySpecialization")
	@ResponseBody
	public List<Map<String, Object>> getDoctorsBySpecialization(@RequestParam("specialization") String specialization,
			HttpSession sessoin) {
		Status status = Status.ACTIVE;
		List<Object[]> doctors = doctorRepository.findDoctorsBySpecialization(specialization,status);
		List<Map<String, Object>> doctorList = new ArrayList<>();

		for (Object[] obj : doctors) {
			Map<String, Object> doctor = new HashMap<>();
			doctor.put("id", obj[0]); // Doctor ID
			doctor.put("name", obj[1]); // Doctor Name
			doctorList.add(doctor);
		}

		return doctorList;
	}

	@GetMapping("/getDoctorFee")
	@ResponseBody
	public Integer getDoctorFee(@RequestParam("doctorId") Integer doctorId, HttpSession session) {
		session.setAttribute("doctorId", doctorId);
		return doctorRepository.findDoctorFeeById(doctorId);
	}

	@PostMapping("/appointment")
	public String addAppointment(@ModelAttribute("appointment") Appointment appointment, BindingResult bindingResult,
			Model model, HttpSession session) {

		try {
			if (bindingResult.hasErrors()) {
				System.out.println("Error: " + bindingResult.toString());
				model.addAttribute("appointment", appointment);
				return "redirect:/patient/patientprofile";
			}

			// Fetch patient and doctor using IDs(Assuming they are passed in request)

			int doctorId = Integer.parseInt(session.getAttribute("doctorId").toString());
			int patientId = Integer.parseInt(session.getAttribute("patientId").toString());

			Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
			Patient patient = patientRepository.findById(patientId).orElse(null);
			if (doctor == null || patient == null) {
				session.setAttribute("message", new Message("Doctor or patient not found!", "alert-danger"));
				return "redirect:/patient/patientprofile";
			}

			appointment.setDoctor(doctor);
			appointment.setPatient(patient);

			AppointmentStatus status = AppointmentStatus.SCHEDULED;
			appointment.setAppointmentStatus(status);
			// Save Appointment
			System.out.println("Appointment: " + appointment);
			Appointment result = this.appointmentService.saveAppointment(appointment);
			session.setAttribute("appointmentId", result.getId());
			System.out.println("Result: " + result);

			model.addAttribute("appointment", new Appointment());
			session.setAttribute("message", new Message("Successfully Registered!!!", "alert-success"));

			return "redirect:/patient/patientprofile"; // Redirect to patient profile

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
			return "redirect:/patient/patientprofile";
		}
	}

//	Appointment Status 
//	@GetMapping("/appointmentstatus")
//	@ResponseBody
//	public List<Appointment> getAppointments(HttpSession session) {
//		Patient p1 = (Patient) session.getAttribute("loggedInPatient");
//		List<Appointment> appointments = appointmentService.getAppointmentsByPatient(p1);
//
//		// Add Appointment objects to 'a'
//		for (Appointment appointment : appointments) {  
//		    System.out.println("-----------------"+appointment);  
//		}
//
//	    return appointments;
//	}
//	@GetMapping("/appointmentstatus")
//	@ResponseBody
//	public List<Appointment> getAppointments(HttpSession session) {
//	    Patient p1 = (Patient) session.getAttribute("loggedInPatient");
//
//	    if (p1 == null) {
//	        return new ArrayList<>(); // ✅ Prevent NullPointerException
//	    }
//
//	    List<Appointment> appointments = appointmentService.getAppointmentsByPatient(p1);
//	    appointments.forEach(System.out::println); // ✅ Better logging
//
//	    return appointments;
//	}
	@GetMapping("/appointmentstatus")
	@ResponseBody
	public List<Map<String, Object>> getAppointments(HttpSession session) {
		Patient p1 = (Patient) session.getAttribute("loggedInPatient");
		if (p1 == null) {
			return List.of(); // Prevent NullPointerException
		}

		List<Appointment> appointments = appointmentService.getAppointmentsByPatient(p1);
		// Convert to a format that includes doctor details
		List<Map<String, Object>> response = new ArrayList<>();
		for (Appointment a : appointments) {
			Map<String, Object> data = new HashMap<>();
			data.put("appointmentId", a.getId());
			data.put("doctorName", a.getDoctor().getName());
			data.put("specialization", a.getDoctor().getSpecialization());
			data.put("date", a.getDate());
			data.put("time", a.getTime());
			data.put("consultantFee", a.getConsultantFee());
			data.put("payment", "Paid");
			data.put("appointmentStatus", a.getAppointmentStatus());
			response.add(data);
		}
		return response;
	}

//	@GetMapping("/getalldoctors")
//	@ResponseBody
//	public List<Map<String,Object>> getAllDoctors() {
//		System.out.println("------------------------------------------------->1");
//	    List<Object[]> doctors = this.doctorService.getAllDoctorsDetail();
//	    System.out.println("------------------------------------------------->2");
//	    List<Map<String,Object>> response = new ArrayList<>();
//	    for (Object[] a : doctors) {
//	        Map<String, Object> data = new HashMap<>();
//	        data.put("Name", a.getName());
//	        data.put("Specialization", a.getSpecialization());
//	        data.put("ConsultantFees", a.getFees());
//	        response.add(data);
//	    }
//	    return response;
//	}

	@GetMapping("/getalldoctors")
	@ResponseBody
	public List<Map<String, Object>> getAllDoctors() {
		System.out.println("------------------------------------------------->1");
		Status status = Doctor.Status.ACTIVE;
		List<Object[]> doctors = this.doctorService.getAllDoctorsDetail(status);
		System.out.println("------------------------------------------------->2");

		List<Map<String, Object>> response = new ArrayList<>();

		for (Object[] a : doctors) {
			Map<String, Object> data = new HashMap<>();

			data.put("Name", (String) a[2]); // Name is likely a String (should be at index 2)
			data.put("Specialization", (String) a[0]); // Specialization is likely a String (index 0)
			data.put("ConsultantFees", ((Number) a[1])); // Fees should be converted properly

			response.add(data);
		}
		return response;
	}

	/* Update Patient Detail */

	@GetMapping("/updatepatient")
	@ResponseBody
	public ResponseEntity<?> getPatientForUpdate(HttpSession session) {
		int patientId = Integer.parseInt(session.getAttribute("patientId").toString());
		Patient patient = patientRepository.findById(patientId).orElse(null);
		Map<String, Object> data = new HashMap<>();
		data.put("pid", patient.getpId());
		data.put("name", patient.getName());
		data.put("dob", patient.getDob());
		data.put("gender", patient.getGender());
		data.put("bloodGroup", patient.getBloodGroup());
		data.put("emailId", patient.getEmailId());
		data.put("address", patient.getAddress());
		data.put("mobileNo", patient.getMobileNo());
		return ResponseEntity.ok(data);
	}

	@PostMapping("/update-patient")
	public String updatePatient(@RequestParam("pId") int pid, @RequestParam("name") String name,
			@RequestParam("dob") String dob, @RequestParam("gender") String gender,
			@RequestParam("bloodGroup") String bloodGroup, @RequestParam("emailId") String emailId,
			@RequestParam("address") String address, @RequestParam("mobileNo") long mobileNo) {
		System.out.println("##################################################1");
		patientService.updatePatientDetail(pid, name, dob, gender, bloodGroup, emailId, address, mobileNo);

		return "redirect:/patient/patientprofile";
	}

	
//	razorpay integration
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String , Object> data) throws RazorpayException {
		System.out.println(data);
		int amt = Integer.parseInt(data.get("amount").toString());
		var client = new RazorpayClient("rzp_test_yv9samGMeSJty0","itd9AJhDFb0QWOLv5lxuPGO8");
		JSONObject ob = new JSONObject();
		ob.put("amount", amt*100);
		ob.put("currency","INR");
		ob.put("receipt","txn_235425");
		
		//creating new Order
		Order order = client.orders.create(ob);
		System.out.println(order);
		System.out.println("Hey Order function executed");
//		if you want you can save this to your data..
		return order.toString();
	}
	
	
	
	
	/* Receipt of payment */
	@GetMapping("/receipt-of-payment")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> receiptOfPayment(HttpSession session){
		Patient p1 = (Patient) session.getAttribute("loggedInPatient");
		
		List<Appointment> appointments = appointmentService.getAppointmentsByPatient(p1);
		// Convert to a format that includes doctor details
		List<Map<String, Object>> response = new ArrayList<>();
		for (Appointment a : appointments) {
			Map<String, Object> data = new HashMap<>();
			data.put("receiptNo", a.getId());
			data.put("date", LocalDate.now().toString());
			data.put("ammount", a.getConsultantFee());
			data.put("method", "upi");
			response.add(data);
		}
		return ResponseEntity.ok(response);
	}
}
