package com.hospital.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Patient;
import com.hospital.management.entities.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
	/*
	 * @Query("SELECT a FROM Prescription a JOIN FETCH a.doctor WHERE a.patient = :patient"
	 * ) Prescription findByPatientWithDoctor(@Param("patient") int i);
	 */
	
	@Modifying
	@Query("UPDATE Prescription p SET p.treatment = :treatment, p.medicine = :medicine, p.advice = :advice, p.remark = :remark WHERE p.preId = :pid")
	void updatePrescription(@Param("pid") long pid, 
	                        @Param("treatment") String treatment, 
	                        @Param("medicine") String medicine, 
	                        @Param("advice") String advice, 
	                        @Param("remark") String remark);
	@Query("SELECT p FROM Prescription p JOIN FETCH p.doctor WHERE p.patient.pId = :patientId")
	Prescription findByPatientWithDoctor(@Param("patientId") int patientId);


}
