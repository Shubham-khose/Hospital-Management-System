package com.hospital.management.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.management.entities.Patient;
import com.hospital.management.entities.Patient.Status;

import jakarta.transaction.Transactional;
@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer>{
	Patient findByPId(Long patientId);
	Optional<Patient> findByEmailId(String email);
	@Query(value = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'patients'", nativeQuery = true)
    List<String> getPatientColumnNames();
	
	@Modifying
	@Query("UPDATE Patient p SET p.name = :name, p.dob = :dob, p.gender = :gender, " +
	       "p.bloodGroup = :bloodGroup, p.emailId = :emailId, p.address = :address, " +
	       "p.mobileNo = :mobileNo WHERE p.pId = :pid")
	void updatePatient(@Param("pid") int pId,
	                   @Param("name") String name,
	                   @Param("dob") String dob, 
	                   @Param("gender") String gender, 
	                   @Param("bloodGroup") String bloodGroup, 
	                   @Param("emailId") String emailId, 
	                   @Param("address") String address, 
	                   @Param("mobileNo") long mobileNo);
	
	@Modifying
	@Transactional  // Ensures query execution inside a transaction
	@Query("UPDATE Patient p SET p.admittedDate = :admittedDate, p.status = :status WHERE p.pId = :pid")
	void updatePatient(@Param("pid") int pId, @Param("admittedDate") Date admittedDate, @Param("status") Status status);


//	Optional<Patient> updatePatient();
		
//	for Records 
	@Query("SELECT COUNT(p) FROM Patient p WHERE FUNCTION('YEAR', p.admittedDate) = :year AND FUNCTION('MONTH', p.admittedDate) = :month")
	int countPatientsTreated(@Param("year") int year, @Param("month") int month);

}
