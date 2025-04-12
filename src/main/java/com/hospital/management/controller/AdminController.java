package com.hospital.management.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hospital.management.entities.Admin;
import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Doctor;
import com.hospital.management.entities.Doctor.Status;
//import javax.validation.Valid;
import com.hospital.management.entities.Patient;
import com.hospital.management.healper.Message;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.services.AdminService;
import com.hospital.management.services.AppointmentService;
import com.hospital.management.services.DoctorService;
import com.hospital.management.services.PatientServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class AdminController {
	
	@Autowired
	public PatientServices patientService;	
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private DoctorService doctorService;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired 
	private AppointmentService appointmentService;
	@GetMapping("/")
	public String home() {

		return "home";
	}

	@GetMapping("/log")
	public String GetLoginPages(HttpSession session, Model model) {
	    model.addAttribute("message", session.getAttribute("message"));
	    session.removeAttribute("message");  // Clears session message after retrieving
	    return "login_pages";
	}

//	it should add in patient controller
	@GetMapping("/reg")
	 public String showPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "registration"; // Ensure patients.html exists in src/main/resources/templates/
    }
	
	/* ########################################################################################*/
	
	@PostMapping("/admin-log")
	public String adminLogin(@RequestParam("emailId") String email, @RequestParam("password") String password, Model model,
			HttpServletRequest request) {
		try {
			HttpSession session = request.getSession(false);
			if(session != null) {
				session.invalidate();
			}
			session = request.getSession(true);
			Optional<Admin> optionalAdmin = adminService.validateAdmin(email,password);
			if(optionalAdmin.isPresent()) {
				Admin admin = optionalAdmin.get();
				System.out.println("Admin Detail: "+optionalAdmin.get());
				session.setAttribute("loginAdmin", admin);
				return "redirect:/admin-profile";
			}else {
				request.getSession().setAttribute("message",new Message("Invalid email or password ","alert-danger"));
				return "login_pages";
			}
		}catch(Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("message", new Message("Something went wrong: "+e.getMessage(),"alert-danger"));
			return "login_pages";
		}
	}

	/* Admin Profile */
	@GetMapping("/admin-profile")
	public String getAdminProfile(HttpServletRequest request,Model model) {
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("loginAdmin") == null) {			
			return "redirect:/log";
		}		
		Admin admin = (Admin) session.getAttribute("loginAdmin");
		model.addAttribute("AdminDetail",admin);	
		return "admin_home_page";	
	}
	
	/* Add Doctor */
	
	@PostMapping("/add-doctor")
	public String addDoctor(@ModelAttribute("doctor") Doctor doctor,BindingResult bindingResult,
			@RequestParam(value="agreement",defaultValue="false") boolean agreement,
			Model model,HttpSession session) {
		try {
			if(!agreement) {
				System.out.println("You have not agreed the terms and conditions ");
				throw new Exception("You have not agreed the terms and conditions");
			}
			
			if(bindingResult.hasErrors()) {
				System.out.println("ERROR "+bindingResult.toString());
				model.addAttribute("doctor",doctor);
			}
			
			System.out.println("doctor: "+doctor);
			doctor.setAddedDate(new Date());
			doctor.setStatus(Status.ACTIVE);
			Doctor saveDoctor = this.doctorService.saveDoctor(doctor);
			System.out.println("Saved Doctor==>"+saveDoctor);
			model.addAttribute("doctor",new Doctor());
			session.setAttribute("message",new Message("Successfully Rgistered Doctor","alert-success"));
			return "redirect:/admin-profile";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("doctor",doctor);
			session.setAttribute("message",new Message("something went wrong "+e.getMessage(),"alert-danger"));
			return "redirect:/admin-profile";
		}
	}
		/* ########################################################################################*/
		/* Update Doctor */
		@GetMapping("/updatedoctor")
		@ResponseBody
		public ResponseEntity<?> getDoctorForUpdate(@RequestParam("doctor") Doctor doctor,HttpSession session){
			 System.out.println("Doctor#######: "+doctor);
		    Doctor doctor1 = doctorRepository.findById(doctor.getdId()).orElse(null);
		    if (doctor1 == null || doctor1.getStatus() == Doctor.Status.REMOVED) {
				System.out.println("One");
				session.setAttribute("message", new Message("Doctor Not Found", "alert-danger"));
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Patient not found\"}");
			}

			Map<String, Object> data = new HashMap<>();
			data.put("did", doctor1.getdId());
			data.put("language", doctor1.getLanguage());
			data.put("name", doctor1.getName());
			data.put("age", doctor1.getAge());
			data.put("gender", doctor1.getGender());
			data.put("specialization", doctor1.getSpecialization());
			data.put("emailId", doctor1.getEmailId());
			data.put("experience", doctor1.getExperience());
			data.put("mobileNo", doctor1.getMobileNo());
			
			return ResponseEntity.ok(data);
		}
		
		@PostMapping("/update-doctor")
		public String updateDoctor(
				@RequestParam("dId") int did,
		        @RequestParam("name") String name,
		        @RequestParam("age") int age, 
		        @RequestParam("gender") String gender, 
		        @RequestParam("specialization") String specialization, 
		        @RequestParam("experience") int experience, 
		        @RequestParam("emailId") String emailId, 
		        @RequestParam("language") String language,
		        @RequestParam("mobileNo") long mobileNo) {
			System.out.println("##################################################1");
		    doctorService.updateDoctorDetail(did, name, age, gender, specialization,experience, emailId ,language, mobileNo);

		    return "redirect:/admin-profile";
		}
	
		/* Remove Doctor */
		
		@PostMapping("/remove-doctor")
		public String removeDoctor(
				@RequestParam("dId") int did) {
			System.out.println("##################################################");
		    doctorService.removeDoctorDetail(did);

		    return "redirect:/admin-profile";
		}
	
	
		/*	**************************************************************************/
//	For Records 
		@GetMapping("/monthly-report")
		public ResponseEntity<?> getMonthlyReport(@RequestParam int year, @RequestParam int month) {
		    Map<String, Object> report = new HashMap<>();
		    report = adminService.getMothlyReportDetail(year,month);		   
		    return ResponseEntity.ok(report);
		}

	
	
	
//	For getting paymentREquests 
		
		@GetMapping("/payment-requests")
		@ResponseBody
		public ResponseEntity<?> getPaymentRequest(){
			List<Appointment> appointment = appointmentService.getPaymentRequests();
			List<Map<String,Object>> response = new ArrayList<>();
		    for (Appointment a : appointment) {
		        Map<String, Object> data = new HashMap<>();
		        data.put("PatientName", a.getPatient().getName());
		        data.put("DoctorName", a.getDoctor().getName());
		        data.put("Payment", a.getConsultantFee());
		        data.put("PayStatus", a.getAppointmentStatus());
		        response.add(data);
		    }
			return ResponseEntity.ok(response);
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/******************************************************************************************/
	
	
	/* Logout Method */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session != null) {
	        session.invalidate(); // Destroy session
	    }
	    return "redirect:/log"; // Redirect to login page
	}
	
	
	/* for Clearing error messages after reloading */
	@PostMapping("/clear-session-message")
	public void clearSessionMessage(HttpSession session) {
	    session.removeAttribute("message");
	}
}
