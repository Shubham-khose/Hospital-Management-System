package com.hospital.management.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.management.entities.Patient;
import com.hospital.management.entities.Patient.Status;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientServices implements PatientServicesImpl {
	@Autowired
	public PatientRepository patientRepository;
	
//	@Autowired 
//	private DoctorRepository doctorRepository;
	
	public Patient savePatient(Patient patient) {
		Patient result1 = patientRepository.save(patient);
		return result1;
	}

	public Optional<Patient> getUserByEmailId(String email) {
		return patientRepository.findByEmailId(email);
	}

	public Optional<Patient> validatePatient(String email, String password) {
		Optional<Patient> patientOptional = patientRepository.findByEmailId(email);

		if (patientOptional.isPresent()) {
			Patient patient = patientOptional.get();
			if (patient.getPassword().equals(password)) { // Direct match without hashing
				return Optional.of(patient);
			}
		}
		return Optional.empty();
	}

	
//	  public Optional<Patient> getPatientByEmail(String email) { Optional<Patient>
//	 patientOptional = patientRepository.findByEmailId(email);
//	  
//	  if(patientOptional.isPresent()) { Patient patient = patientOptional.get();
//	 return Optional.of(patient); } return Optional.empty(); }
//	 

//	
//	  public List<String> getPatientHeaders() { return
//	  patientRepository.getPatientColumnNames(); }
	
	public Patient getPatientById(long pId) {
		Patient patient = patientRepository.findByPId(pId);
		return patient;
	}
	
	@Transactional
	public void updatePatientDetail(int pid, 
	                                String name,
	                                String dob, 
	                                String gender, 
	                                String bloodGroup, 
	                                String emailId, 
	                                String address, 
	                                long mobileNo) {
		System.out.println("##################################################2");
	    System.out.println(pid + " " + name + " " + dob + " " + gender + " " + bloodGroup + " " + emailId + " " + address + " " + mobileNo);
	    patientRepository.updatePatient(pid, name, dob, gender, bloodGroup, emailId, address, mobileNo);
	}

	  @Transactional
	    public void updatePatientDetail(int patientId, Date admittedDate, Status status) {
	        patientRepository.updatePatient(patientId, admittedDate, status);
	    }

//	 
}
