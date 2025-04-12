package com.hospital.management.controller;


import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Doctor;
import com.hospital.management.entities.Patient;
import com.hospital.management.entities.Patient.Status;
import com.hospital.management.entities.Prescription;
import com.hospital.management.healper.Message;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.services.AppointmentService;
import com.hospital.management.services.DoctorService;
import com.hospital.management.services.PatientServices;
import com.hospital.management.services.PrescriptionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/doctor")
public class DoctorsController {

	@Autowired
	private PatientServices patientService;
	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private PrescriptionService prescriptionService;

	@PostMapping("/doctor-log")
	public String handleDoctorLogin(@RequestParam("emailId") String email, @RequestParam("password") String password,
			Model model, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			session = request.getSession(true);
			Optional<Doctor> doctorOptional = doctorService.validateDoctor(email, password);

			if (doctorOptional.isPresent()) {
				Doctor doctor = doctorOptional.get();
				System.out.println("doctor_detail" + doctorOptional.get());
				session.setAttribute("doctorLogin", doctor);

				return "redirect:/doctor/doctorprofile";
			} else {
				request.getSession().setAttribute("message", new Message("Invalid email or password", "alert-danger"));
				return "login_pages";

			}

		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("message",
					new Message("Something Went Wrong: " + e.getMessage(), "alert-danger"));
			return "login_pages";
		}
	}

	@GetMapping("/doctorprofile")
	public String getDoctorProfile(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false); // Get session without creating a new one
		System.out.println("Doctor In==============================================>");
		if (session == null || session.getAttribute("doctorLogin") == null) {
			System.out.println("No logged-in patient found in session.");
			return "redirect:/log"; // Redirect if not logged in
		}

		Doctor doctor = (Doctor) session.getAttribute("doctorLogin");
		System.out.println("Doctor In==============================================>"+doctor);
		// for access in appointment as foreign key
		session.setAttribute("prescriptionDid", doctor.getdId());

		model.addAttribute("doctor", doctor);
		model.addAttribute("status", "Confirm");
		// model.addAttribute("tableHeaders", tableHeaders);

		return "doctor_home_page"; // Return the profile page
	}

	@GetMapping("/getPatient-description")
	@ResponseBody
	public Map<String, Object> getePatientDescription(long pId, HttpSession session) {
		Patient patient = patientService.getPatientById(pId);
		session.setAttribute("PrescriptionPid", patient.getpId());
		Map<String, Object> data = new HashMap<>();
		data.put("patientName", patient.getName());

		System.out.println("patinet:---->" + patient);
		return data;
	}

//	@GetMapping("/search-prescription")
//	@ResponseBody
//	public Prescription getPrescription(@RequestParam("patient") Patient patient, Model model, HttpSession session) {
//		System.out.println(patient);
//		Prescription prescription = this.prescriptionService.getPrescription(patient);
//		if (patient == null) {
//			model.addAttribute("patientStatus", "Patient is Not found--------");
//			System.out.println("One");
//			return prescription;
//		}
//		if(prescription == null) {
//			System.out.println("two");
//			return prescription;
//		}
//		System.out.println(prescription);
//		/*
//		 * model.addAttribute("patientStatus", "Patient is Not found");
//		 * session.setAttribute("message", new Message("Patient Not found",
//		 * "alert-danger"));
//		 */
//		Map<String, Object> data = new HashMap<>();
//		data.put("patientId", prescription.getPatient().getpId());
//		data.put("patientName", prescription.getPatient().getName());
//		data.put("treatementName", prescription.getTreatment());
//		data.put("medecine", prescription.getMedicine());
//		data.put("remark", prescription.getRemark());
//		data.put("advice",prescription.getAdvice());
//
//		System.out.println("patinet:---->" + patient);
//		Prescription p1 = (Prescription) data;
//		System.out.println("prescription"+p1);
//		return p1;
//	}

	@GetMapping("/search-prescription")
	@ResponseBody
	public ResponseEntity<?> getPrescription(@RequestParam("patient") Patient patient, HttpSession session) {
		System.out.println(patient);
		Prescription prescription = this.prescriptionService.getPrescription(patient);

		if (patient == null) {
			System.out.println("One");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Patient not found\"}");
		}

		if (prescription == null) {
			System.out.println("Two - Prescription not found");
			return ResponseEntity.ok("{}"); // Return an empty JSON object instead of null
		}
//		session.setAttribute("updatePrescriptionId", prescription.getpreId());
		Map<String, Object> data = new HashMap<>();
		data.put("prescriptionId", prescription.getPreId());
		data.put("patientId", prescription.getPatient().getpId());
		data.put("patientName", prescription.getPatient().getName());
		data.put("treatmentName", prescription.getTreatment());
		data.put("medicine", prescription.getMedicine());
		data.put("remark", prescription.getRemark());
		data.put("advice", prescription.getAdvice());

		return ResponseEntity.ok(data);
	}

	@PostMapping("/update-prescription")
	public String updatePrescription(@RequestParam("preId") long pid,
	        @RequestParam("treatment") String treatment, 
	        @RequestParam("medicine") String medicine, 
	        @RequestParam("advice") String advice, 
	        @RequestParam("remark") String remark,HttpSession session){
	   
	    // Call the service layer to update the prescription
	    prescriptionService.updatePrescription(pid, treatment, medicine, advice, remark);
	    
	    // Redirect back to the doctor profile page
	    return "redirect:/doctor/doctorprofile";
	}


//	 @PostMapping("/add-prescription")
//	 public String addPrescription(@ModelAttribute("prescription") Prescription prescription, BindingResult bindingResult, Model model,
//			HttpSession session) {
//		 try {
//			 Prescription result = this.prescriptionService.savePrescription(prescription);
//			 System.out.println("Prescription"+result);
//			 return "redirect:/doctor/patientprofile";
//		 }catch(Exception e) {
//			 e.printStackTrace();
//			 return "doctor_home_page";
//		 }
//		
//	 }

	@PostMapping("/add-prescription")
	public String addPrescription(@ModelAttribute("prescription") Prescription prescription, HttpSession session) {
		try {
			int doctorId = Integer.parseInt(session.getAttribute("prescriptionDid").toString());
			int patientId = Integer.parseInt(session.getAttribute("PrescriptionPid").toString());

			System.out.println("========================================================");
			System.out.println("d" + doctorId + "p" + patientId);
			System.out.println("========================================================");
			Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
			Patient patient = patientRepository.findById(patientId).orElse(null);
			if (doctor == null && patient == null) {
				session.setAttribute("message", new Message("Doctor or patient not found!", "alert-danger"));
				return "redirect:/docotr/doctorprofile";
			}
			prescription.setDoctor(doctor);
			prescription.setPatient(patient);
			Prescription result = prescriptionService.savePrescription(prescription);
			System.out.println("Prescription: " + result);
			return "redirect:/doctor/doctorprofile";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/doctor/doctorprofile";
		}
	}
	
//	get my appointments 
	@GetMapping("/doctor-appointments")
	@ResponseBody
	public List<Map<String, Object>> getDoctorAppointments(HttpSession session) {
	    Doctor d1 = (Doctor) session.getAttribute("doctorLogin");
	    if (d1 == null) {
	        return List.of(); // Prevent NullPointerException
	    }
	    
	    List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(d1);	    
	    // Convert to a format that includes doctor details
	    List<Map<String, Object>> response = new ArrayList<>();
	    for (Appointment a : appointments) {
	        Map<String, Object> data = new HashMap<>();
	        data.put("pId", a.getPatient().getpId());
	        data.put("patientName", a.getPatient().getName());
	        data.put("date", a.getDate());
	        data.put("time", a.getTime());
	        data.put("status",a.getPatient().getStatus());
	        response.add(data);
	    }   
	    return response;
	}
		
	/*
	 * @GetMapping("/search-prescription")
	 * 
	 * @ResponseBody public ResponseEntity<?>
	 * getPrescription(@RequestParam("patient") Long patientId, Model model,
	 * HttpSession session) { System.out.println("Searching for patient ID: " +
	 * patientId);
	 * 
	 * // Fetch patient by ID Patient patient =
	 * patientRepository.findByPId(patientId).orElse(null);
	 * 
	 * if (patient == null) { session.setAttribute("message", new
	 * Message("Patient Not Found", "alert-danger")); return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).
	 * body("{\"message\": \"Patient not found\"}"); }
	 * 
	 * ResponseEntity<?> prescription = (ResponseEntity<?>)
	 * prescriptionService.getPrescription(patient);
	 * 
	 * if (prescription == null) { session.setAttribute("message", new
	 * Message("Prescription Not Found", "alert-warning")); return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).
	 * body("{\"message\": \"Prescription not found\"}"); }
	 * 
	 * return ResponseEntity.ok(prescription); }
	 */
	@PostMapping("/doctor-patient-admit")
	public ResponseEntity<?> admitPatient(@RequestBody Map<String, Integer> request) {
	    int patientId = request.get("patientId");

	    Optional<Patient> optionalPatient = patientRepository.findById(patientId);
	    if (optionalPatient.isPresent()) {
	        Patient patient = optionalPatient.get();
	        Date admittedDate = new Date();
	        Status status = Status.ACTIVE;

	        patient.setAdmittedDate(admittedDate);
	        patient.setStatus(status);

	        patientService.updatePatientDetail(patientId, admittedDate, status);

	        return ResponseEntity.ok(Map.of("success", true));
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false));
	    }
	}


	
}
