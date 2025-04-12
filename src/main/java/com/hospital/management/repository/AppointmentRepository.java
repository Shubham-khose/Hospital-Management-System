package com.hospital.management.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Appointment.AppointmentStatus;
import com.hospital.management.entities.Patient.Status;

import jakarta.transaction.Transactional;

import com.hospital.management.entities.Doctor;
import com.hospital.management.entities.Patient;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
//	List<Appointment> findByPatient(Patient patient ); // i should use this format for access appointment by patient not use only id because it will access all object rather than only id  
//	@Query("SELECT a FROM Appointment a JOIN FETCH a.doctor WHERE a.patient = :patient")
//	List<Appointment> findByPatientWithDoctor(@Param("patient") Patient patient);
 // ✅ Recommended
	@Query("SELECT a FROM Appointment a JOIN FETCH a.doctor WHERE a.patient = :patient")
	List<Appointment> findByPatientWithDoctor(@Param("patient") Patient patient);
	
	@Query("SELECT a FROM Appointment a JOIN FETCH a.patient WHERE a.doctor = :doctor")
	List<Appointment> findAppointmentByDoctor(@Param("doctor") Doctor doctor);

	@Modifying
	@Transactional  // Ensures query execution inside a transaction
	@Query("UPDATE Appointment a SET a.appointmentStatus = :appointmentStatus WHERE a.id = :appointmentId")
	void updateAppointmentStatus(@Param("appointmentId") long appointmentId, @Param("appointmentStatus") AppointmentStatus appointmentStatus);

	/* For Records */
	@Query("SELECT COUNT(a) FROM Appointment a WHERE FUNCTION('YEAR', a.date) = :year AND FUNCTION('MONTH', a.date) = :month")
    int countAppointmentsTaken(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentStatus = 'CANCELLED' AND FUNCTION('YEAR', a.date) = :year AND FUNCTION('MONTH', a.date) = :month")
    int countAppointmentsCancelled(@Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(d.fees) FROM Appointment a JOIN a.doctor d WHERE FUNCTION('YEAR', a.date) = :year AND FUNCTION('MONTH', a.date) = :month AND a.appointmentStatus = 'SCHEDULED'")
    Double totalFeeCollected(@Param("year") int year, @Param("month") int month);

    
	/* Get Appointment Requests */
    @Query("SELECT a FROM Appointment a ORDER BY a.id DESC")
    List<Appointment> getAppointmentReq();



}
