package com.hospital.management.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.management.entities.Admin;
import com.hospital.management.repository.AdminRepository;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;

@Service
public class AdminService implements AdminServiceImpl{
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired AppointmentRepository appointmentRepository;
	
	
	
	public Optional<Admin> validateAdmin(String email,String password){
		
		return adminRepository.findByEmailIdAndPassword(email, password);
	}

	public Map<String, Object> getMothlyReportDetail(int year, int month) {
		 Map<String, Object> report = new HashMap<>();
		 //it was added because of patient admitted there is no chance for discharge this that's whay i comment out this
//		 report.put("TotalPatientsTreated", patientRepository.countPatientsTreated(year, month));

		 report.put("TotalPatientsTreated", appointmentRepository.countAppointmentsTaken(year, month));
		    report.put("TotalDoctorsAdded", doctorRepository.countDoctorsAdded(year, month));
		    report.put("TotalDoctorsRemoved", doctorRepository.countDoctorsRemoved(year, month));
		    report.put("TotalAppointmentsTaken", appointmentRepository.countAppointmentsTaken(year, month));
		    report.put("TotalAppointmentsCancelled", appointmentRepository.countAppointmentsCancelled(year, month));
		    report.put("TotalFeeCollected", appointmentRepository.totalFeeCollected(year, month));

		return report;
	}
}
