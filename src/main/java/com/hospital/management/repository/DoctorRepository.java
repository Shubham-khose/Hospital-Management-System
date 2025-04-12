package com.hospital.management.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.hospital.management.entities.Doctor;
import com.hospital.management.entities.Doctor.Status;

import jakarta.transaction.Transactional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer>{
	
	Optional<Doctor> findByEmailId(String email);
	Optional<Doctor> findByEmailIdAndPasswordAndStatus(String email, String password, Status status);
	
	@Query("SELECT DISTINCT d.specialization FROM Doctor d WHERE d.status = :status")
	List<String> findDistinctSpecialization(@Param("status") Status status);
	
	/* getting doctors name according to specializatoin */
	
	@Query("SELECT d.dId, d.name FROM Doctor d WHERE d.specialization = :specialization AND d.status = :status")
	List<Object[]> findDoctorsBySpecialization(@Param("specialization") String specialization,
	                                           @Param("status") Status status);

	 
		/* getting doctor fees form doctor id */
	 
	 @Query("SELECT d.fees FROM Doctor d WHERE d.dId = :doctorId")
	 Integer findDoctorFeeById(@Param("doctorId") Integer doctorId);
	 
	 @Query(value = "SELECT d.specialization, d.fees, d.name FROM doctors d WHERE d.status = :status", nativeQuery = true)
	 List<Object[]> getAllDoctors(@Param("status") String status); // Change Status to String

	 @Modifying
	 @Query("UPDATE Doctor d SET d.name = :name, d.age = :age, d.gender = :gender, " +
	        "d.specialization = :specialization, d.emailId = :emailId, d.language = :language, " +
	        "d.experience = :experience, d.mobileNo = :mobileNo WHERE d.dId = :did")
	 void updateDoctor(
	         @Param("did") int dId,  // Changed "pid" to "did" to match the parameter name
	         @Param("name") String name,
	         @Param("age") int age, 
	         @Param("gender") String gender, 
	         @Param("specialization") String specialization, 
	         @Param("experience") int experience, 
	         @Param("emailId") String emailId, 
	         @Param("language") String language, 
	         @Param("mobileNo") long mobileNo);
	
	 @Modifying
		@Transactional  // Ensures query execution inside a transaction
		@Query("UPDATE Doctor d SET d.removeDate = :removeDate, d.status = :status WHERE d.dId = :did")
		void removeDoctor(@Param("did") int dId, @Param("removeDate") Date removeDate, @Param("status") Status status);


		/* For Records */
	 @Query("SELECT COUNT(d) FROM Doctor d WHERE FUNCTION('YEAR', d.addedDate) = :year AND FUNCTION('MONTH', d.addedDate) = :month")
	    int countDoctorsAdded(@Param("year") int year, @Param("month") int month);

	    @Query("SELECT COUNT(d) FROM Doctor d WHERE FUNCTION('YEAR', d.removeDate) = :year AND FUNCTION('MONTH', d.removeDate) = :month")
	    int countDoctorsRemoved(@Param("year") int year, @Param("month") int month);
	
}
