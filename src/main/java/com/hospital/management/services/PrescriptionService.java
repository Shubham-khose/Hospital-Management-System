package com.hospital.management.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.management.entities.Patient;
import com.hospital.management.entities.Prescription;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PrescriptionRepository;

import jakarta.transaction.Transactional;

@Service
public class PrescriptionService implements PrescriptionServiceImpl {
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private PrescriptionRepository prescriptionRepository;

	@Override
	public Prescription getPrescription(Patient patient) {
		Patient patient1 = patientRepository.findById(patient.getpId()).orElse(null);
		System.out.println("prescription--->"+patient1);
		if (patient1 != null) {
			Prescription prescription = prescriptionRepository.findByPatientWithDoctor(patient1.getpId());
			System.out.println("############"+prescription);
			if (prescription != null) {
				System.out.println("============================================================================================================================================");
				return prescription;
			} else {
				System.out.println("NULLL");
				return null;
			}
		}
		return null;
	}

	public Prescription savePrescription(Prescription prescription) {
		Prescription result1 = prescriptionRepository.save(prescription);
		return result1;
	}
	@Transactional  // Ensures the update query runs within a transaction
	public void updatePrescription(long pid, 
            String treatment, 
            String medicine, 
            String advice, 
            String remark) {
		System.out.println("prescription:--->"+pid+treatment+medicine+advice+remark);
		prescriptionRepository.updatePrescription(pid,treatment,medicine,advice,remark);
	}
	/*
	 * @Override public ResponseEntity<?> getPrescription(Patient patient) { Patient
	 * patient1 = patientRepository.findById(patient.getpId()).orElse(null); if
	 * (patient1 != null) { Prescription prescription =
	 * prescriptionRepository.findByPatientWithDoctor(patient1); if (prescription !=
	 * null) { return ResponseEntity.ok(prescription); } else { return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).
	 * body("{\"message\": \"Prescription not found\"}"); } } return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).
	 * body("{\"message\": \"Patient not found\"}"); }
	 */

}
