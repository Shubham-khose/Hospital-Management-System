package com.hospital.management.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Appointment.AppointmentStatus;
import com.hospital.management.entities.Patient.Status;
import com.hospital.management.entities.Doctor;
import com.hospital.management.entities.Patient;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService implements AppointmentServiceImpl{

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	public Appointment saveAppointment(Appointment appointment) {
		Appointment appointment1 = appointmentRepository.save(appointment);
		return appointment1;
	}
	
	
//	public List<Appointment> getAppointmentsByPatient(Patient p1){	
//		Patient patient = patientRepository.findById(p1.getpId()).orElse(null); //it not works in Optional<Patient> like	
//		if(patient != null) {
//			return appointmentRepository.findByPatientWithDoctor(patient);
//		}		
//		return List.of();//Return Empty List if patient is not found 
//	}
	
//	public List<Appointment> getAppointmentsByPatient(Patient p1) {
//	    Patient patient = patientRepository.findById(p1.getpId()).orElse(null);
//	    if (patient != null) {
//	        List<Appointment> appointments = appointmentRepository.findByPatientWithDoctor(patient);
//	        System.out.println("Hellooooooooooooooo");
//	        appointments.forEach(a -> System.out.println(a.getDoctor())); // Debugging
//	        return appointments;
//	    }
//	    return List.of();
//	}

	public List<Appointment> getAppointmentsByPatient(Patient p1) {    
	    Patient patient = patientRepository.findById(p1.getpId()).orElse(null);   
	    if (patient != null) {
	        List<Appointment> appointments = appointmentRepository.findByPatientWithDoctor(patient);
	        appointments.forEach(a -> System.out.println(
	        		"AppointmentId: "+a.getId()+
	            "Doctor: " + a.getDoctor().getName() + 
	            ", Specialization: " + a.getDoctor().getSpecialization() + 
	            ", Date: " + a.getDate() + 
	            ", Time: " + a.getTime() + 
	            ", Fee: " + a.getConsultantFee()
	        )); // ✅ Debugging: Check if doctor details are fetched
	        return appointments;
	    }
	    return List.of(); // Empty list if no appointments found
	}
	
	public List<Appointment> getAppointmentsByDoctor(Doctor d1) {    
	    Doctor doctor = doctorRepository.findById(d1.getdId()).orElse(null);   
	    if (doctor != null) {
	        List<Appointment> appointments = appointmentRepository.findAppointmentByDoctor(doctor);
	        appointments.forEach(a -> System.out.println(
	        		"AppointmentId: "+a.getId()+
	            "Doctor: " + a.getDoctor().getName() + 
	            ", Specialization: " + a.getDoctor().getSpecialization() + 
	            ", Date: " + a.getDate() + 
	            ", Time: " + a.getTime() + 
	            ", Fee: " + a.getConsultantFee()
	        )); // ✅ Debugging: Check if doctor details are fetched
	        return appointments;
	    }
	    return List.of(); // Empty list if no appointments found
	}

	@Transactional
	public void cancelAppointment(long appointmentId, AppointmentStatus appointmentStatus) {
		appointmentRepository.updateAppointmentStatus(appointmentId, appointmentStatus);		
	}


	public List<Appointment> getPaymentRequests() {
		List<Appointment> appointment = appointmentRepository.getAppointmentReq();
		for(Appointment a:appointment){
			System.out.println(a);
		}
		
		return appointment;
	}
	

}
