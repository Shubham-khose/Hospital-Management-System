package com.hospital.management.entities;
import java.sql.Date;

import com.hospital.management.entities.Doctor.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    private String specialization;
    private String consultantFee;
    private String time;
    
    @Enumerated(EnumType.STRING) // ✅ Stores as 'ACTIVE' instead of index (0,1)
    @Column(length = 20) 
    private AppointmentStatus appointmentStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pId")
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dId")
    private Doctor doctor;


	public Appointment(Long id, Date date, String specialization, String consultantFee, String time,
			AppointmentStatus appointmentStatus, Patient patient, Doctor doctor) {
		super();
		this.id = id;
		this.date = date;
		this.specialization = specialization;
		this.consultantFee = consultantFee;
		this.time = time;
		this.appointmentStatus = appointmentStatus;
		this.patient = patient;
		this.doctor = doctor;
	}

	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	public AppointmentStatus getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public Date getDate() {
		return date;
	}

	public Date setDate(Date date) {
		return this.date = date;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getConsultantFee() {
		return consultantFee;
	}

	public void setConsultantFee(String consultantFee) {
		this.consultantFee = consultantFee;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	
	@Override
	public String toString() {
	    return "Appointment [id=" + id + 
	           ", date=" + date + 
	           ", specialization=" + specialization + 
	           ", consultantFee=" + consultantFee + 
	           ", appointmentStatus=" + appointmentStatus+
	           ", time=" + time + 
	           ", patientId=" + (patient != null ? patient.getpId() : "null") +  // Only print patient ID
	           ", doctorId=" + (doctor != null ? doctor.getdId() : "null") +  // Only print doctor ID
	           "]";
	}
	/* in above rather than print object we print only id because at the time of print 
	 * 
	 *  "full object it should have some time so we get only id thats why error not occurs 
	 *  Hibernate doesn’t load full objects immediately (lazy loading).
		If you print a lazy-loaded object in toString(), Hibernate tries to fetch it but fails if there’s no active database transaction.
		Solution: Print only IDs instead of full objects in toString().
 */
	public enum AppointmentStatus {
		SCHEDULED,  // ✅ Defined correctly
	    CANCELLED;
	}
	
    
}

