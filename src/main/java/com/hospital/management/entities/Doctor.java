package com.hospital.management.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hospital.management.entities.Patient.Status;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dId;

	private String name;
	private int age;
	private int experience;
	private String specialization;
	private String gender;
	private String schedule;
	
	@Column(length = 15, unique = true)
	private String mobileNo; // Changed from long to String

	@Column(length = 100, unique = true, nullable = false)
	private String emailId;

	private String language;
	 @Temporal(TemporalType.DATE)
	    private Date addedDate;

	    @Temporal(TemporalType.DATE)
	    private Date removeDate;

	    @Enumerated(EnumType.STRING) // ✅ Stores as 'ACTIVE' instead of index (0,1)
	    @Column(length = 20) 
	    private Status status;
	private String password;
	private int fees;

	// One doctor can have multiple appointments
	@OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Appointment> appointments = new ArrayList<>();

	// One doctor has one prescription
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "pre_id")  // Ensures 'pre_id' is mapped in doctors table
//	private Prescription prescription;
	@OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Prescription prescription;

	
	// Default Constructor
	public Doctor() {
	}

	// Parameterized Constructor
	
	public Doctor(int dId, String name, int age, int experience, String specialization, String gender, String schedule,
			String mobileNo, String emailId, String language, Date addedDate, Date removeDate, Status status,
			String password, int fees, List<Appointment> appointments, Prescription prescription) {
		super();
		this.dId = dId;
		this.name = name;
		this.age = age;
		this.experience = experience;
		this.specialization = specialization;
		this.gender = gender;
		this.schedule = schedule;
		this.mobileNo = mobileNo;
		this.emailId = emailId;
		this.language = language;
		this.addedDate = addedDate;
		this.removeDate = removeDate;
		this.status = status;
		this.password = password;
		this.fees = fees;
		this.appointments = appointments;
		this.prescription = prescription;
	}

	// Getters and Setters
	
	
	
	public int getdId() {
		return dId;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Date getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(Date removeDate) {
		this.removeDate = removeDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setdId(int dId) {
		this.dId = dId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getFees() {
		return fees;
	}

	public void setFees(int fees) {
		this.fees = fees;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	@Override
	public String toString() {
		return "Doctor [dId=" + dId + ", name=" + name + ", age=" + age + ", experience=" + experience
				+ ", specialization=" + specialization + ", gender=" + gender + ", schedule=" + schedule + ", mobileNo="
				+ mobileNo + ", emailId=" + emailId + ", language=" + language + ", addedDate=" + addedDate
				+ ", removeDate=" + removeDate + ", status=" + status + ", password=" + password + ", fees=" + fees
				+ ", appointments=" + appointments + ", prescription=" + prescription + "]";
	}
	
	
	public enum Status {
	    ACTIVE,  // ✅ Defined correctly
	    REMOVED;
	}
	
} 