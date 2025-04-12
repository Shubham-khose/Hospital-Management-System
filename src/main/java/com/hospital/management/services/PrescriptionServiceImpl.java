package com.hospital.management.services;

import org.springframework.http.ResponseEntity;

import com.hospital.management.entities.Patient;
import com.hospital.management.entities.Prescription;

public interface PrescriptionServiceImpl {
	/* ResponseEntity<?> getPrescription(Patient patient); */

	Prescription getPrescription(Patient patient);
}
