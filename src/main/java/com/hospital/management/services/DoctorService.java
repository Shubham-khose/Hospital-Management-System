package com.hospital.management.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Doctor;
import com.hospital.management.entities.Doctor.Status;
import com.hospital.management.entities.Patient;
import com.hospital.management.repository.DoctorRepository;

import jakarta.transaction.Transactional;

@Service
public class DoctorService implements DoctorServicesImpl{
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	public Doctor saveDoctor(Doctor doctor) {
		Doctor doctor1 = doctorRepository.save(doctor);
		return doctor1;
	}
	
	public Optional<Doctor> validateDoctor(String email, String password) {
	    Status status = Doctor.Status.ACTIVE;
	    return doctorRepository.findByEmailIdAndPasswordAndStatus(email, password, status);
	}


//	public List<Object[]> getAllDoctorsDetail() {
//	    List<Object[]> doctors = this.doctorRepository.getAllDoctors();
//		 
////		 doctors.forEach(a ->
////		 System.out.println("AppointmentId----------->: "+a.getName()));
//		 
//	    return doctors;
//	}

	public List<Object[]> getAllDoctorsDetail(Status status) {
	    List<Object[]> doctors = this.doctorRepository.getAllDoctors(status.name()); // Convert Enum to String because by Enum we could not able to search in table using sql 

	    // Debugging: Print values correctly using index-based access
	    doctors.forEach(a -> 
	        System.out.println("Doctor Name----------->: " + (String) a[2]) // Name is at index 2
	    );

	    return doctors;
	}
	
	@Transactional
	public void updateDoctorDetail(int did,  String name, int age, String gender, 
	                               String specialization,int experience, String emailId, String language, long mobileNo) {
	    
	    doctorRepository.updateDoctor(did, name, age, gender, specialization, experience,emailId,language, mobileNo);
	}

	@Transactional
	public void removeDoctorDetail(int did) {
		Doctor doctor = doctorRepository.getById(did);
		Date removeDate = new Date();
		Status status = Status.REMOVED;
		
		doctor.setAddedDate(removeDate);
		doctor.setStatus(status);
		doctorRepository.removeDoctor(did,removeDate,status);		
	}


}
